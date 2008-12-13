package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.HashMap;
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
    
    private final Map<Class<? extends Annotation>, List<Janitor>> mapOfJanitors;
    
    private final Parameters bindingParameters;
    
    public GuiceletModule(
            HttpServletRequest request,
            HttpServletResponse response,
            Map<Class<? extends Annotation>, List<Janitor>> mapOfJanitors,
            Parameters bindingParameters)
    {
        this.request = request;
        this.response = response;
        this.mapOfJanitors = mapOfJanitors;
        this.bindingParameters = bindingParameters; 
    }

    @Override
    protected void configure()
    {
        final Map<Class<? extends Annotation>, Parameters> parameters = new HashMap<Class<? extends Annotation>, Parameters>();

        bindScope(RequestScoped.class, new RequestScope(parameters, request));
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
        

        for (Class<? extends Annotation> annotation : mapOfJanitors.keySet())
        {
            final List<Janitor> janitors = mapOfJanitors.get(annotation); 
            bind(JanitorQueue.class)
                .annotatedWith(annotation)
                .toProvider(new Provider<JanitorQueue>()
                {
                    public JanitorQueue get()
                    {
                        return new JanitorQueue(janitors);
                    }
                });
        }
        
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
            .toInstance(Headers.fromRequest(request));
        
        bind(Headers.class)
            .annotatedWith(Response.class)
            .toInstance(new Headers(request.getMethod()));
        
        bind(Parameters.class)
            .annotatedWith(Binding.class)
            .toProvider(new Provider<Parameters>()
                {
                    public Parameters get()
                    {
                        return bindingParameters;
                    }
                })
            .in(RequestScoped.class);

        bind(Parameters.class)
            .annotatedWith(Request.class)
            .toProvider(new Provider<Parameters>()
                {
                    public Parameters get()
                    {
                        return Parameters
                                .fromStringArrayMap(getParameterMap(request));
                    }
                })
            .in(RequestScoped.class);
        
        bind(new TypeLiteral<Map<Class<? extends Annotation>, Parameters>>() { })
            .toProvider(new Provider<Map<Class<? extends Annotation>, Parameters>>()
                {
                    public Map<Class<? extends Annotation>, Parameters> get()
                    {
                        return parameters;
                    }
                })
            .in(RequestScoped.class);
    }
    
    @SuppressWarnings("unchecked")
    private final Map<String, String[]> getParameterMap(HttpServletRequest request)
    {
        return request.getParameterMap();
    }
}
