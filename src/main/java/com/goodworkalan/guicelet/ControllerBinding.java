package com.goodworkalan.guicelet;

public class ControllerBinding
{
    private final int priority;
    
    private final Class<?> controller;
    
    public ControllerBinding(int priority, Class<?> controller)
    {
        this.priority = priority;
        this.controller = controller;
    }
    
    public int getPriority()
    {
        return priority;
    }
    
    public Class<?> getController()
    {
        return controller;
    }
}
