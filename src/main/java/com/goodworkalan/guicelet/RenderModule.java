package com.goodworkalan.guicelet;

import com.google.inject.AbstractModule;

public abstract class RenderModule extends AbstractModule
{
    private final ViewConditionBinder viewBinder;
    
    public RenderModule(ViewConditionBinder viewBinder)
    {
        this.viewBinder = viewBinder;
    }
    
    public ViewConditionBinder end()
    {
        return viewBinder;
    }
}
