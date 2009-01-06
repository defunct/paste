package com.goodworkalan.guicelet;

import java.util.List;

import com.google.inject.AbstractModule;

public class GuiceletModule extends AbstractModule
{
    private final SessionScope sessionScope;
    
    private final BasicScope requestScope;
    
    private final BasicScope controllerScope;
    
    private final List<Janitor> servletJanitors;
    
    public GuiceletModule(SessionScope sessionScope,
                          BasicScope requestScope,
                          BasicScope controllerScope,
                          List<Janitor> servletJanitors)
    {
        this.sessionScope = sessionScope;
        this.requestScope = requestScope;
        this.controllerScope = controllerScope;
        this.servletJanitors = servletJanitors;
    }

    @Override
    protected void configure()
    {
        bindScope(SessionScoped.class, sessionScope);
        bindScope(RequestScoped.class, requestScope);
        bindScope(ControllerScoped.class, controllerScope);
        
        bind(JanitorQueue.class)
            .annotatedWith(Servlet.class)
            .toInstance( new JanitorQueue(servletJanitors));
    }
}
