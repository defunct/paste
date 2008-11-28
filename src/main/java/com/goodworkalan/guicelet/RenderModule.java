package com.goodworkalan.guicelet;

import com.google.inject.AbstractModule;

public abstract class RenderModule extends AbstractModule
{
    private final ViewBinder viewBinder;
    
    public RenderModule(ViewBinder viewBinder)
    {
        this.viewBinder = viewBinder;
    }
    
    public ViewPathBinder bind(String pattern)
    {
        return viewBinder.bind(pattern);
    }
}
