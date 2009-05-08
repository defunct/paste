package com.goodworkalan.guicelet;

// TODO Document.
public class ViewBinding
{
    // TODO Document.
    private final int priority;
    
    // TODO Document.
    private final RenderModule module;
    
    // TODO Document.
    public ViewBinding(int priority, RenderModule module)
    {
        this.priority = priority;
        this.module = module;
    }
    
    // TODO Document.
    public int getPriority()
    {
        return priority;
    }
    
    // TODO Document.
    public RenderModule getModule()
    {
        return module;
    }
}
