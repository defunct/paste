package com.goodworkalan.guicelet;


public class ViewBinding
{
    private final int priority;
    
    private final RenderModule module;
    
    public ViewBinding(int priority, RenderModule module)
    {
        this.priority = priority;
        this.module = module;
    }
    
    public int getPriority()
    {
        return priority;
    }
    
    public RenderModule getModule()
    {
        return module;
    }
}
