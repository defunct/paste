package com.goodworkalan.paste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Mapping;
import com.goodworkalan.dovetail.TreeMapper;
import com.goodworkalan.paste.faults.Faults;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.paste.redirect.Redirector;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;

// TODO Document.
public class PasteGuicer
{
    // TODO Document.
    private final SessionScope sessionScope;

    // TODO Document.
    private final BasicScope requestScope;
    
    // TODO Document.
    private final BasicScope controllerScope;
    
    // TODO Document.
    private final Injector injector;

    // TODO Document.
    private final List<GlobTree<RuleMap<ControllerBinding>>> controllerBindings;

    // TODO Document.
    private final RuleMap<ViewBinding> mapOfViewBindings;

    // TODO Document.
    private final List<Janitor> janitors;
    
    private final ThreadLocal<Integer> callDepth;

    // TODO Document.
    public PasteGuicer(List<GlobTree<RuleMap<ControllerBinding>>> controllerBindings,
                          RuleMap<ViewBinding> mapOfViewBindings,
                          List<Module> modules)
    {
        this.requestScope = new BasicScope();
        this.controllerScope = new BasicScope();
        this.sessionScope = new SessionScope();
        this.janitors = new ArrayList<Janitor>();
        this.callDepth = new ThreadLocal<Integer>();
        
        List<Module> pushGuicletModule = new ArrayList<Module>(modules);
        pushGuicletModule.add(new PasteModule(sessionScope, requestScope, controllerScope, janitors));
        
        this.injector = Guice.createInjector(pushGuicletModule);

        this.controllerBindings = controllerBindings;
        this.mapOfViewBindings = mapOfViewBindings;
    }

    // TODO Document.
    public void filter(HttpServletRequest request,
                       HttpServletResponse response,
                       FilterChain chain)
        throws IOException, ServletException
    {
        Interception interception = new Interception();
        filter(new InterceptingRequest(interception, request, controllerScope),
               new InterceptingResponse(interception, response),
               interception, chain);
    }

    // TODO Document.
    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        Interception interception,
                        FilterChain chain)
        throws IOException, ServletException
    {
        Integer depth = callDepth.get();
        if (depth == null)
        {
            depth = 0;
            callDepth.set(depth);
        }
        callDepth.set(depth + 1);
        if (depth == 0)
        {
            sessionScope.enter(request);
        }
        else
        {
            requestScope.push(Key.get(JanitorQueue.class),
                              Key.get(HttpServletRequest.class),
                              Key.get(HttpServletResponse.class),
                              Key.get(ServletRequest.class),
                              Key.get(ServletResponse.class));
        }
        List<Janitor> janitors = new ArrayList<Janitor>();
        try
        {
            filter(request, response, janitors, interception, chain, depth);
        }
        finally
        {
            cleanUp(janitors);
            if (depth == 0)
            {
                requestScope.exit();
                sessionScope.exit();
            }
            else
            {
                requestScope.pop();
            }
            controllerScope.exit();
            callDepth.set(depth);
        }
    }
    
    // TODO Document.
    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        List<Janitor> janitors,
                        Interception interception,
                        FilterChain chain, 
                        int depth)
        throws IOException, ServletException
    {
        String path = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (path == null)
        {
            path = request.getRequestURI();
            String contextPath = request.getContextPath();
            if (contextPath != null)
            {
                path = path.substring(contextPath.length());
            }
        }

        Throwable throwable = null;
        boolean hasController = false;
        for (GlobTree<RuleMap<ControllerBinding>> tree : controllerBindings)
        {
            if (interception.isIntercepted())
            {
                break;
            }

            TreeMapper<RuleMap<ControllerBinding>> mapper = new TreeMapper<RuleMap<ControllerBinding>>();
            if (tree.match(mapper, path))
            {
                long highest = Long.MIN_VALUE;
                Class<?> controllerClass = null;
                Map<String, String> mappings = null;

                for (Mapping<RuleMap<ControllerBinding>> mapping : mapper.mappings())
                {
                    List<ControllerBinding> bindings
                        = mapping.getObject()
                            .test().put(BindKey.METHOD, request.getMethod())
                                   .put(BindKey.PATH, path)
                                   .get();
                    for (ControllerBinding binding : bindings)
                    {
                        if (binding.getPriority() > highest)
                        {
                            highest = binding.getPriority();
                            controllerClass = binding.getController();
                            mappings = mapping.getParameters();
                        }
                    }
                }

                if (highest == Long.MIN_VALUE)
                {
                    break;
                }

                if (hasController)
                {
                    controllerScope.exit();
                }
                else
                {
                    enterRequest(requestScope, request, response, janitors);
                }

                throwable = enterController(controllerScope, injector, controllerClass, mappings);
                if (throwable != null)
                {
                    break;
                }

                Object controller = injector.getInstance(Key.get(Object.class, Controller.class));
                hasController = true;

                Actors actors = controller.getClass().getAnnotation(Actors.class);
                if (actors != null)
                {
                    for (Class<?  extends Actor> actor : actors.value())
                    {
                        throwable = injector.getInstance(actor).actUpon(controller);
                        if (throwable != null)
                        {
                            break;
                        }
                    }
                }
            }
        }

        if (throwable instanceof Redirection)
        {
            ((Redirection) throwable).redirect(injector.getInstance(Redirector.class));
        }

        if (hasController || throwable != null)
        {
            Object controller = hasController ? injector.getProvider(Key.get(Object.class, Controller.class)).get() : null;
            
            List<ViewBinding> views
                = mapOfViewBindings
                    .test()
                        .put(BindKey.PACKAGE, hasController ? controller.getClass().getPackage().getName() : null)
                        .put(BindKey.CONTROLLER_CLASS, hasController ? controller.getClass() : null)
                        .put(BindKey.PATH, path)
                        .put(BindKey.EXCEPTION, throwable != null ? throwable.getClass() : throwable)
                        .put(BindKey.METHOD, request.getMethod()).get();
            
            SortedMap<Integer, ViewBinding> prioritized = new TreeMap<Integer, ViewBinding>(Collections.reverseOrder());
            for (ViewBinding view : views)
            {
                prioritized.put(view.getPriority(), view);
            }

            if (prioritized.size() != 0)
            {
                ViewBinding view = prioritized.get(prioritized.firstKey());
                injector.createChildInjector(view.getModule())
                        .getInstance(Renderer.class).render();
            }
            else if (throwable != null)
            {
                if (throwable instanceof RuntimeException)
                {
                    throw (RuntimeException) throwable;
                }
                else if (throwable instanceof Error)
                {
                    throw (Error) throwable;
                }
            }
        }
        
        if (!interception.isIntercepted())
        {
            chain.doFilter(request, response);
        }
    }
    
    // TODO Document.
    private void cleanUp(List<Janitor> listOfJanitors)
    {
        for (Janitor janitor : listOfJanitors)
        {
            try
            {
                janitor.cleanUp();
            }
            catch (ThreadDeath t)
            {
                throw t;
            }
            catch (Throwable t)
            {
            }
        }
    }
    
    // TODO Document.
    public void destroy()
    {
        cleanUp(janitors);
    }

    // TODO Document.
    public static void enterRequest(
                BasicScope scope,
                HttpServletRequest request,
                HttpServletResponse response,
                List<Janitor> requestJanitors) throws IOException
    {
        scope.enter();
        
        scope.seed(ServletContext.class, request.getServletContext());
        
        scope.seed(HttpServletRequest.class, request);
        scope.seed(ServletRequest.class, request);
        
        scope.seed(HttpSession.class, request.getSession(true));
            
        scope.seed(HttpServletResponse.class, response);
        scope.seed(ServletResponse.class, response);
        
        String path = request.getRequestURI().substring(request.getContextPath().length());
        scope.seed(Key.get(String.class, Path.class), path);

        scope.seed(Key.get(String.class, WelcomeFile.class), "index");

        ArrayList<NamedValue> parameters = new ArrayList<NamedValue>();
        
        for (Map.Entry<String, String[]> entry : Request.getParameterMap(request).entrySet())
        {
            for (String value : entry.getValue())
            {
                parameters.add(new NamedValue(NamedValue.REQUEST, entry.getKey(), value));
            }
        }
        
        scope.seed(Parameters.class, new Parameters(parameters));

        scope.seed(JanitorQueue.class, new JanitorQueue(requestJanitors));
        
        scope.seed(Key.get(new TypeLiteral<Map<Object, Object>>() { }, Faults.class), new HashMap<Object, Object>());
    }


    /**
     * Enter the controller scope creating a controller. Returns the Throwable
     * thrown by the controller constructor if any. This unorthodox return value
     * distinguishes an exception raised in the controller constructor from an
     * exception raised during the creation of other objects in the method. That
     * is, it is an explicit return of an exception raised during construction,
     * that may be processed by the error handling of the web application.
     * 
     * @param scope
     *            The controller scope.
     * @param injector
     *            The Guice injector.
     * @param controllerClass
     *            The class of the controller to create.
     * @param mappings
     *            The map of parameter bindings for the controller.
     * @return The exception raised by controller, if any.
     */
    public static Throwable enterController(BasicScope scope, Injector injector, Class<?> controllerClass, Map<String, String> mappings)
    {
        scope.enter();
        
        List<NamedValue> parameters = new ArrayList<NamedValue>();
        for (Map.Entry<String, String> entry : mappings.entrySet())
        {
            parameters.add(new NamedValue(NamedValue.DOVETAIL, entry.getKey(), entry.getValue()));
        }
        
        for (NamedValue namedValue : injector.getInstance(Parameters.class))
        {
            parameters.add(namedValue);
        }
        
        scope.seed(Key.get(new TypeLiteral<List<NamedValue>>() {}, Controller.class), parameters);
        scope.seed(Key.get(Parameters.class, Controller.class), new Parameters(parameters));
        
        try
        {
            scope.seed(Key.get(Object.class, Controller.class), injector.getInstance(controllerClass));
        }
        catch (ProvisionException e)
        {
            if (e.getCause() != null)
            {
                return e.getCause();
            }
            throw e;
        }
        
        return null;
    }
}