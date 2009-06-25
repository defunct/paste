package com.goodworkalan.paste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Match;
import com.goodworkalan.paste.intercept.InterceptingRequest;
import com.goodworkalan.paste.intercept.InterceptingResponse;
import com.goodworkalan.paste.intercept.Interception;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.paste.redirect.Redirector;
import com.goodworkalan.paste.stop.Abnormality;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

// TODO Document.
public class PasteGuicer
{
    // TODO Document.
    private final ServletContext servletContext;
    
    // TODO Document.
    private final Map<String, String> initialization;
    
    // TODO Document.
    private final SessionScope sessionScope;

    // TODO Document.
    private final BasicScope requestScope;
    
    // TODO Document.
    private final BasicScope controllerScope;
    
    // TODO Document.
    private final Injector injector;

    // TODO Document.
    private final List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> controllerBindings;

    // TODO Document.
    private final RuleMap<Pair<Integer, RenderModule>> mapOfViewBindings;

    // TODO Document.
    private final List<Janitor> janitors;
    
    private final ThreadLocal<Integer> callDepth;

    // TODO Document.
    public PasteGuicer(Routes routes, List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> controllerBindings,
                          RuleMap<Pair<Integer, RenderModule>> mapOfViewBindings,
                          List<Module> modules, ServletContext servletContext, Map<String, String> initialization)
    {
        this.requestScope = new BasicScope();
        this.controllerScope = new BasicScope();
        this.sessionScope = new SessionScope();
        this.janitors = new ArrayList<Janitor>();
        this.callDepth = new ThreadLocal<Integer>();
        this.servletContext = servletContext;
        this.initialization = initialization;
        
        List<Module> pushGuicletModule = new ArrayList<Module>(modules);
        pushGuicletModule.add(new PasteModule(routes, sessionScope, requestScope, controllerScope, janitors));
        
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
                              Key.get(ServletResponse.class),
                              Key.get(String.class, Path.class));
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

        ScopeManager scopeManager = new ScopeManager(requestScope, request, response, path, janitors, servletContext, initialization);

        Throwable throwable = null;
        boolean hasController = false;
        for (GlobTree<RuleMap<Pair<Integer, Class<?>>>> tree : controllerBindings)
        {
            if (interception.isIntercepted())
            {
                break;
            }

            List<Match<RuleMap<Pair<Integer, Class<?>>>>> matches = tree.newGlobber(new GuiceMatchTestFactory(scopeManager, injector)).map(path);
            if (!matches.isEmpty())
            {
                long highest = Long.MIN_VALUE;
                Class<?> controllerClass = null;
                Map<String, String> mappings = null;

                for (Match<RuleMap<Pair<Integer, Class<?>>>> mapping : matches)
                {
                    List<Pair<Integer, Class<?>>> bindings
                        = mapping.getObject()
                            .test().put(BindKey.METHOD, request.getMethod())
                                   .put(BindKey.PATH, path)
                                   .get();
                    for (Pair<Integer, Class<?>> binding : bindings)
                    {
                        if (Tuple.get1(binding) > highest)
                        {
                            highest = Tuple.get1(binding);
                            controllerClass = Tuple.get2(binding);
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
                    scopeManager.enterRequest();
                }

                hasController = false;
                throwable = scopeManager.enterController(controllerScope, injector, controllerClass, mappings);
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
            injector.getInstance(Redirector.class).redirect(((Redirection) throwable).getWhere());
        }
        else if (throwable instanceof Abnormality)
        {
            injector.getInstance(Response.class).setStatus(((Abnormality) throwable).getStatus());
        }
            

        if (hasController || throwable != null)
        {
            Object controller = hasController ? injector.getProvider(Key.get(Object.class, Controller.class)).get() : null;
            
            List<Pair<Integer, RenderModule>> views
                = mapOfViewBindings
                    .test()
                        .put(BindKey.PACKAGE, hasController ? controller.getClass().getPackage().getName() : null)
                        .put(BindKey.CONTROLLER_CLASS, controller)
                        .put(BindKey.PATH, path)
                        .put(BindKey.STATUS, injector.getInstance(Response.class).getStatus())
                        .put(BindKey.EXCEPTION, throwable != null ? throwable.getClass() : null)
                        .put(BindKey.METHOD, request.getMethod())
                        .get();
            
            SortedMap<Integer, Pair<Integer, RenderModule>> prioritized = new TreeMap<Integer, Pair<Integer, RenderModule>>(Collections.reverseOrder());
            for (Pair<Integer, RenderModule> view : views)
            {
                prioritized.put(Tuple.get1(view), view);
            }

            if (prioritized.size() != 0)
            {
                Pair<Integer, RenderModule> view = prioritized.get(prioritized.firstKey());
                injector.createChildInjector(Tuple.get2(view))
                        .getInstance(Renderer.class)
                        .render();
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

}
