package com.goodworkalan.guicelet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class GuiceletFilter implements Filter
{
    // TODO Document.
    private static final long serialVersionUID = 20081122L;
    
    // TODO Document.
    private GuiceletGuicer guicer;
    
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
        
        CoreBinder binder = new CoreBinder();
        for (Dispatcher dispatcher : listOfDispatchers)
        {
            dispatcher.bind(binder);
        }
        
        guicer = new GuiceletGuicer(binder.getBindingTrees(),
                                    binder.getMapOfRules(),
                                    listOfModules);
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
