package com.goodworkalan.guicelet;

import com.google.inject.AbstractModule;

public abstract class RenderModule extends AbstractModule
{
    private final ViewBinder end;
    
    public RenderModule(ViewBinder viewBinder)
    {
        this.end = viewBinder;
    }
    
    public ViewBinder end()
    {
        return end;
    }
}
