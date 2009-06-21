package com.goodworkalan.paste;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.faults.Faults;
import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

// TODO Document.
public class PasteModule extends AbstractModule
{
    // TODO Document.
    private final SessionScope sessionScope;
    
    // TODO Document.
    private final BasicScope requestScope;
    
    // TODO Document.
    private final BasicScope controllerScope;
    
    // TODO Document.
    private final List<Janitor> servletJanitors;
    
    // TODO Document.
    public PasteModule(SessionScope sessionScope,
                          BasicScope requestScope,
                          BasicScope controllerScope,
                          List<Janitor> servletJanitors)
    {
        this.sessionScope = sessionScope;
        this.requestScope = requestScope;
        this.controllerScope = controllerScope;
        this.servletJanitors = servletJanitors;
    }

    // TODO Document.
    @Override
    protected void configure()
    {
        bindScope(SessionScoped.class, sessionScope);
        bindScope(RequestScoped.class, requestScope);
        bindScope(ControllerScoped.class, controllerScope);
        
        bind(new TypeLiteral<Map<String, String>>() {})
            .annotatedWith(InitializationParameters.class)
            .toProvider(new NullProvider<Map<String, String>>())
            .in(RequestScoped.class);
        bind(ServletContext.class)
            .toProvider(new NullProvider<ServletContext>())
            .in(RequestScoped.class);
        
        bind(JanitorQueue.class)
            .annotatedWith(Servlet.class)
            .toInstance(new JanitorQueue(servletJanitors));
        
        // Need to bind to null provider so that they are bound when you 
        // set the request scope.
        bind(HttpSession.class)
            .toProvider(new NullProvider<HttpSession>())
            .in(RequestScoped.class);
        
        bind(HttpServletRequest.class)
            .toProvider(new NullProvider<HttpServletRequest>())
            .in(RequestScoped.class);
        bind(ServletRequest.class)
            .toProvider(new NullProvider<ServletRequest>())
            .in(RequestScoped.class);
        
        bind(HttpServletResponse.class)
            .toProvider(new NullProvider<HttpServletResponse>())
            .in(RequestScoped.class);
        bind(ServletResponse.class)
            .toProvider(new NullProvider<ServletResponse>())
            .in(RequestScoped.class);
        
        bind(new TypeLiteral<Map<String, String[]>>() {})
            .toProvider(new NullProvider<Map<String, String[]>>())
            .in(RequestScoped.class);

        bind(String.class).annotatedWith(Path.class)
            .toProvider(new NullProvider<String>())
            .in(RequestScoped.class);
        bind(String.class).annotatedWith(WelcomeFile.class)
            .toProvider(new NullProvider<String>())
            .in(RequestScoped.class);

        bind(Parameters.class)
            .toProvider(new NullProvider<Parameters>())
            .in(RequestScoped.class);
        
        bind(Parameters.class)
            .annotatedWith(Controller.class)
            .toProvider(new NullProvider<Parameters>())
            .in(ControllerScoped.class);
        
        bind(new TypeLiteral<List<NamedValue>>() { })
            .annotatedWith(Controller.class)
            .toProvider(new NullProvider<List<NamedValue>>())
            .in(ControllerScoped.class);
      
        bind(JanitorQueue.class)
            .toProvider(new NullProvider<JanitorQueue>())
            .in(RequestScoped.class);
        
        bind(new TypeLiteral<Map<Object, Object>>() { }).annotatedWith(Faults.class)
            .toProvider(new NullProvider<Map<Object,Object>>())
            .in(RequestScoped.class);

        bind(Object.class)
            .annotatedWith(Controller.class)
            .toProvider(new NullProvider<Parameters>())
            .in(ControllerScoped.class);
    }
}
