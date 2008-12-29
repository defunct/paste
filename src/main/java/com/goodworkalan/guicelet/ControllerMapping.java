package com.goodworkalan.guicelet;

public class ControllerMapping
{
    public final Class<?> controller;
    
    public final Parameters parameters;
    
    public ControllerMapping(Class<?> controller, Parameters parameters)
    {
        this.controller = controller;
        this.parameters = parameters;
    }
}
