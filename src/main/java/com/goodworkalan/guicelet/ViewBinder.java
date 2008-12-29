package com.goodworkalan.guicelet;

import com.goodworkalan.diverge.RuleMapBuilder;

public class ViewBinder
{
    private final RuleMapBuilder<ViewBinding> mapOfBindings;
    
    public ViewBinder(RuleMapBuilder<ViewBinding> viewBindings)
    {
        this.mapOfBindings = viewBindings;
    }

    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewConditionBinder(this, mapOfBindings, mapOfBindings.rule()).controller(controllerClass);
    }
    
    
    public  <T extends RenderModule> T with(Class<T> renderClass)
    {
        return new ViewConditionBinder(this, mapOfBindings, mapOfBindings.rule()).with(renderClass);
    }
}
