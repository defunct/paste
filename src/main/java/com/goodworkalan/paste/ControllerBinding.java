package com.goodworkalan.paste;

// FIXME Use Java Tuple.
public class ControllerBinding
{
    // TODO Document.
    private final int priority;
    
    // TODO Document.
    private final Class<?> controller;
    
    // TODO Document.
    public ControllerBinding(int priority, Class<?> controller)
    {
        this.priority = priority;
        this.controller = controller;
    }
    
    // TODO Document.
    public int getPriority()
    {
        return priority;
    }
    
    // TODO Document.
    public Class<?> getController()
    {
        return controller;
    }
}
