package com.goodworkalan.guicelet;

import com.google.inject.AbstractModule;

public class ControllerModule extends AbstractModule
{
    private final Object controller;
    
    public ControllerModule(Object controller)
    {
        this.controller = controller;
    }

    @Override
    protected void configure()
    {
        bindScope(ControllerScoped.class, new ControllerScope());
        bind(Object.class).annotatedWith(Controller.class).toInstance(controller);
    }
}
