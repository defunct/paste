package com.goodworkalan.paste;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Module;

// TODO Document.
public class PasteFilter implements Filter
{
    // TODO Document.
    private static final long serialVersionUID = 20081122L;
    
    // TODO Document.
    private PasteGuicer guicer;
    
    // TODO Document.
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

        List<Router> listOfDispatchers = new ArrayList<Router>();
        String dispatchers = config.getInitParameter("Routers");
        if (dispatchers != null)
        {
            try
            {
                for (String dispatcher : dispatchers.split(","))
                {
                    Class<?> dispatcherClass = Class.forName(dispatcher.trim());
                    listOfDispatchers.add((Router) dispatcherClass.newInstance());
                }
            }
            catch (Exception e)
            {
                throw new ServletException(e);
            }
        }
        
        CoreConnector binder = new CoreConnector();
        for (Router dispatcher : listOfDispatchers)
        {
            dispatcher.connect(binder);
        }
        
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> e = Objects.toStringEnumeration(config.getInitParameterNames());
        while (e.hasMoreElements())
        {
            String name = e.nextElement();
            map.put(name, config.getInitParameter(name));
        }
        
        guicer = new PasteGuicer(binder.getBindingTrees(),
                                    binder.getViewRules(),
                                    listOfModules,
                                    config.getServletContext(),
                                    map);
    }

    // TODO Document.
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
        throws IOException, ServletException
    {   
        guicer.filter((HttpServletRequest) request,
                      (HttpServletResponse) response, chain);
    }
    
    // TODO Document.
    public void destroy()
    {
        guicer.destroy();
    }
}

/* vim: set et tw=80 nowrap: */
