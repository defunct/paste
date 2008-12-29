package com.goodworkalan.guicelet;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.diverge.RuleMap;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Mapping;
import com.goodworkalan.dovetail.TreeMapper;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

public class GuiceletGuicer
{
    private final Injector injector;

    private final List<GlobTree<ControllerBinding>> controllerBindings;

    private final RuleMap<ViewBinding> mapOfViewBindings;

    private final List<Janitor> listOfJanitors;

    public GuiceletGuicer(Injector injector,
                          List<GlobTree<ControllerBinding>> controllerBindings,
                          RuleMap<ViewBinding> viewBindings)
    {
        this.injector = injector;
        this.controllerBindings = controllerBindings;
        this.mapOfViewBindings = viewBindings;
        this.listOfJanitors = new ArrayList<Janitor>();
    }

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

    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        Interception interception,
                        FilterChain chain)
        throws IOException, ServletException
    {
        Map<Class<? extends Annotation>, List<Janitor>> mapOfJanitors = new HashMap<Class<? extends Annotation>, List<Janitor>>();
        mapOfJanitors.put(Servlet.class, listOfJanitors);
        mapOfJanitors.put(Request.class, new ArrayList<Janitor>());
        try
        {
            filter(request, response, mapOfJanitors, interception, chain);
        }
        finally
        {
            cleanUp(mapOfJanitors.get(Request.class));
        }
    }
    
    private void filter(HttpServletRequest request,
                        HttpServletResponse response,
                        Map<Class<? extends Annotation>, List<Janitor>> mapOfJanitors,
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

        Injector childInjector = null;

        for (GlobTree<ControllerBinding> tree : controllerBindings)
        {
            if (interception.isIntercepted())
            {
                break;
            }

            TreeMapper<ControllerBinding> mapper = new TreeMapper<ControllerBinding>();
            if (tree.match(mapper, path))
            {
                SortedMap<Integer, Mapping<ControllerBinding>> prioritized = new TreeMap<Integer, Mapping<ControllerBinding>>(Collections.reverseOrder());
                
                for (Mapping<ControllerBinding> mapping : mapper.mappings())
                {
                    ControllerBinding binding = mapping.getObject();
                    if (binding.test(request, response))
                    {
                        prioritized.put(binding.getPriority(), mapping);
                    }
                }

                if (prioritized.size() != 0)
                {
                    Mapping<ControllerBinding> mapping = prioritized.get(prioritized.firstKey());
                    Module module
                        = new GuiceletModule(
                            request,
                            response,
                            mapOfJanitors,
                            Parameters.fromStringMap(mapping.getParameters()));
                    childInjector = injector.createChildInjector(module);
                    Object controller = childInjector.getInstance(mapping.getObject().getController());
                    module = new ControllerModule(controller);
                    childInjector = childInjector.createChildInjector(module);
                    
                    Actors actors = controller.getClass().getAnnotation(Actors.class);
                    if (actors != null)
                    {
                        for (Class<?  extends Actor> actor : actors.value())
                        {
                            childInjector.getInstance(actor).actUpon(controller);
                        }
                    }
                }
            }
        }

        if (!interception.isIntercepted() && childInjector != null)
        {
            Object controller = childInjector.getProvider(Key.get(Object.class, Controller.class)).get();
            
            List<ViewBinding> views
                = mapOfViewBindings
                    .test()
                        .put(PatternKey.PACKAGE, controller.getClass().getPackage().getName())
                        .put(PatternKey.CONTROLLER, controller.getClass())
                        .put(PatternKey.PATH, path)
                        .put(PatternKey.METHOD, request.getMethod()).get();
            
            SortedMap<Integer, ViewBinding> prioritized = new TreeMap<Integer, ViewBinding>(Collections.reverseOrder());
            for (ViewBinding view : views)
            {
                prioritized.put(view.getPriority(), view);
            }

            if (prioritized.size() != 0)
            {
                ViewBinding view = prioritized.get(prioritized.firstKey());
                Injector viewInjector = childInjector.createChildInjector(view.getModule());
                Renderer renderer = viewInjector.getProvider(Renderer.class).get();
                renderer.render();
            }
        }
        
        if (!interception.isIntercepted())
        {
            chain.doFilter(request, response);
        }
    }
    
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
    
    public void destroy()
    {
        cleanUp(listOfJanitors);
    }
}
