package com.goodworkalan.sprocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Mapping;
import com.goodworkalan.dovetail.TreeMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

// TODO Document.
public class GuiceletGuicer
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

    // TODO Document.
    public GuiceletGuicer(List<GlobTree<RuleMap<ControllerBinding>>> controllerBindings,
                          RuleMap<ViewBinding> mapOfViewBindings,
                          List<Module> modules)
    {
        this.requestScope = new BasicScope();
        this.controllerScope = new BasicScope();
        this.sessionScope = new SessionScope();
        this.janitors = new ArrayList<Janitor>();
        
        List<Module> pushGuicletModule = new ArrayList<Module>(modules);
        pushGuicletModule.add(new GuiceletModule(sessionScope, requestScope, controllerScope, janitors));
        
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
        filter(new InterceptingRequest(interception, request),
               new InterceptingHttpResponse(interception, response),
               interception, chain);
    }

    // TODO Document.
    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        Interception interception,
                        FilterChain chain)
        throws IOException, ServletException
    {
        List<Janitor> janitors = new ArrayList<Janitor>();
        try
        {
            filter(request, response, janitors, interception, chain);
        }
        finally
        {
            cleanUp(janitors);
            requestScope.exit();
            controllerScope.exit();
        }
    }
    
    // TODO Document.
    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        List<Janitor> janitors,
                        Interception interception,
                        FilterChain chain)
        throws IOException, ServletException
    {
        String path = request.getRequestURI();
        
        String contextPath = request.getContextPath();
        if (contextPath != null)
        {
            path = path.substring(contextPath.length());
        }

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
                Parameters parameters = null;
                Class<?> controllerClass = null;

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
                            parameters = Parameters.fromStringMap(mapping.getParameters());
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
                    Scopes.enterRequest(requestScope, request, response, janitors);
                }
                Scopes.enterController(controllerScope, injector, controllerClass, parameters);
                Object controller = injector.getInstance(Key.get(Object.class, Controller.class));
                hasController = true;

                Actors actors = controller.getClass().getAnnotation(Actors.class);
                if (actors != null)
                {
                    for (Class<?  extends Actor> actor : actors.value())
                    {
                        injector.getInstance(actor).actUpon(controller);
                    }
                }
            }
        }

        if (!interception.isIntercepted() && hasController)
        {
            Object controller = injector.getProvider(Key.get(Object.class, Controller.class)).get();
            
            List<ViewBinding> views
                = mapOfViewBindings
                    .test()
                        .put(BindKey.PACKAGE, controller.getClass().getPackage().getName())
                        .put(BindKey.CONTROLLER_CLASS, controller.getClass())
                        .put(BindKey.PATH, path)
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
        }
        
        if (hasController)
        {
            controllerScope.exit();
            requestScope.exit();
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
}
