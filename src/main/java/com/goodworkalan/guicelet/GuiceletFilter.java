package com.goodworkalan.guicelet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.dovetail.Mapping;
import com.goodworkalan.dovetail.TreeMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

public class GuiceletFilter implements Filter
{
    private static final long serialVersionUID = 20081122L;
    
    private Injector injector;
    
    private List<GlobTree<ControllerBinding>> controllerBindings;
    
    private List<GlobTree<ViewBinding>> viewBindings;
    
    final static List<Janitor> listOfJanitors = new ArrayList<Janitor>();
    
    public void init(FilterConfig config) throws ServletException
    {
        List<Module> listOfModules = new ArrayList<Module>();
        String modules = config.getInitParameter("Modules");
        if (modules != null)
        {
            try
            {
                for (String module : modules.split(","))
                {
                    Class<?> moduleClass = Class.forName(module.trim());
                    listOfModules.add((Module) moduleClass.newInstance());
                }
            }
            catch (Exception e)
            {
                throw new ServletException(e);
            }
        }
        injector = Guice.createInjector(listOfModules);

        List<Dispatcher> listOfDispatchers = new ArrayList<Dispatcher>();
        String dispatchers = config.getInitParameter("Dispatchers");
        if (dispatchers != null)
        {
            try
            {
                for (String dispatcher : dispatchers.split(","))
                {
                    Class<?> dispatcherClass = Class.forName(dispatcher.trim());
                    listOfDispatchers.add((Dispatcher) dispatcherClass.newInstance());
                }
            }
            catch (Exception e)
            {
                throw new ServletException(e);
            }
        }
        
        Binder binder = new Binder();
        for (Dispatcher dispatcher : listOfDispatchers)
        {
            dispatcher.bind(binder);
        }
        
        controllerBindings = binder.getBindingTrees();
        viewBindings = binder.getViewBindings();
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException
    {   
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }
    
    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        Interception interception = new Interception();
        doFilter(new InterceptingRequest(interception, request),
                 new InterceptingHttpResponse(interception, response),
                 interception, chain);
    }
    
    private void doFilter(HttpServletRequest request,
            HttpServletResponse response, Interception interception,
            FilterChain chain) throws IOException, ServletException
    {
        List<Janitor> listOfJanitors = new ArrayList<Janitor>();
        try
        {
            String path = request.getRequestURI();
            
            String contextPath = request.getContextPath();
            if (contextPath != null)
            {
                path = path.substring(contextPath.length());
            }

            Injector requestInjector = null;

            for (GlobTree<ControllerBinding> tree : controllerBindings)
            {
                if (interception.isIntercepted())
                {
                    break;
                }

                TreeMapper<ControllerBinding> mapper = new TreeMapper<ControllerBinding>();
                if (tree.match(mapper, path))
                {
                    SortedMap<Integer, ControllerBinding> prioritized = new TreeMap<Integer, ControllerBinding>(Collections.reverseOrder());
                    
                    for (Mapping<ControllerBinding> mapping : mapper.mappings())
                    {
                        ControllerBinding binding = mapping.getObject();
                        if (binding.test(request, response))
                        {
                            prioritized.put(binding.getPriority(), binding);
                        }
                    }

                    if (prioritized.size() != 0)
                    {
                        ControllerBinding binding = prioritized.get(prioritized.firstKey());
                        Object controller = injector.getInstance(binding.getController());
                            
                        GuiceletModule module = new GuiceletModule(request, response, listOfJanitors, controller);
                        requestInjector = injector.createChildInjector(module);
                        
                        Actors actors = controller.getClass().getAnnotation(Actors.class);
                        if (actors != null)
                        {
                            for (Class<?  extends Actor> actor : actors.value())
                            {
                                requestInjector.getInstance(actor).actUpon(controller);
                            }
                        }
                    }
                }
            }

            if (!interception.isIntercepted() && requestInjector != null)
            {
                Object controller = requestInjector.getProvider(Key.get(Object.class, Controller.class)).get();
                View view = controller.getClass().getAnnotation(View.class);
                if (view != null)
                {
                    for (GlobTree<ViewBinding> tree : viewBindings)
                    {
                        TreeMapper<ViewBinding> mapper = new TreeMapper<ViewBinding>();
                        if (tree.match(mapper, path))
                        {
                            SortedMap<Integer, ViewBinding> prioritized = new TreeMap<Integer, ViewBinding>(Collections.reverseOrder());
                            for (Mapping<ViewBinding> mapping : mapper.mappings())
                            {
                                ViewBinding binding = mapping.getObject();
                                if (binding.test(view.bundle(), view.name()))
                                {
                                    prioritized.put(binding.getPriority(), binding);
                                }
                            }
                            if (prioritized.size() != 0)
                            {
                                ViewBinding binding = prioritized.get(prioritized.firstKey());
                                Injector viewInjector = requestInjector.createChildInjector(binding.getModule());
                                Renderer renderer = viewInjector.getProvider(Renderer.class).get();
                                renderer.render(view.bundle(), view.name());
                            }
                        }
                    }
                }
            }

            if (!interception.isIntercepted())
            {
                chain.doFilter(request, response);
            }
        }
        finally
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
    }
    
    public void destroy()
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
}
