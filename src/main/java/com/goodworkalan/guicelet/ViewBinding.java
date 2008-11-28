package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.List;

public class ViewBinding
{
    private final List<ViewCondition> conditions;
    
    private final int priority;
    
    private final RenderModule module;
    
    public ViewBinding(List<ViewCondition> conditions, int priority, RenderModule module)
    {
        this.conditions = conditions;
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
        for (ViewCondition condition : conditions)
        {
            if (!condition.test(bundle, name))
            {
                return false;
            }
        }
        return true;
    }
}
