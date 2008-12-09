package com.goodworkalan.guicelet;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;

public class GuiceletModule extends AbstractModule
{
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private final Object controller;
    
    private final List<Janitor> listOfJanitors;
    
    public GuiceletModule(HttpServletRequest request, HttpServletResponse response, List<Janitor> listOfJanitors, Object controller)
    {
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.listOfJanitors = listOfJanitors;
    }

    @Override
    protected void configure()
    {
        bindScope(RequestScoped.class, new RequestScope(request));
        bindScope(SessionScoped.class, new SessionScope(request));
        
        bind(HttpServletRequest.class).toInstance(request);
        bind(ServletRequest.class).toInstance(request);
        
        bind(HttpServletResponse.class).toInstance(response);
        bind(ServletResponse.class).toInstance(response);

        bind(new TypeLiteral<Map<String, String[]>>() {})
            .annotatedWith(RequestParameters.class)
            .toProvider(
                new Provider<Map<String, String[]>>()
                {
                    @SuppressWarnings("unchecked")
                    public Map<String, String[]> get()
                    {
                        return (Map<String, String[]>) request.getParameterMap();
                    }
                });
        
        bind(JanitorQueue.class)
            .annotatedWith(ServletContextJanitors.class)
            .toProvider(new Provider<JanitorQueue>()
            {
                public JanitorQueue get()
                {
                    return new JanitorQueue(GuiceletFilter.listOfJanitors);
                }
            });
        bind(JanitorQueue.class)
            .annotatedWith(RequestJanitors.class)
            .toProvider(new Provider<JanitorQueue>()
            {
                public JanitorQueue get()
                {
                    return new JanitorQueue(listOfJanitors);
                }
            });
        
        bind(Object.class)
            .annotatedWith(Controller.class)
            .toInstance(controller);

        String path = request.getRequestURI();
        
        String contextPath = request.getContextPath();
        if (contextPath != null)
        {
            path = path.substring(contextPath.length());
        }
        
        bind(String.class)
            .annotatedWith(Path.class)
            .toInstance(path);
        
        bind(String.class)
            .annotatedWith(WelcomeFile.class)
            .toInstance("index");
        
        bind(Headers.class)
            .annotatedWith(Request.class)
            .toInstance(new Headers(request));
        
        bind(Headers.class)
            .annotatedWith(Response.class)
            .toInstance(new Headers(request.getMethod()));
    }
}
