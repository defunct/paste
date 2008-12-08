package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;

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
    
    public boolean test(Class<? extends Annotation> bundle, String name)
    {
        return true;
    }
}
