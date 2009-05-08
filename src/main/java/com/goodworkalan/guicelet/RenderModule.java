package com.goodworkalan.guicelet;

import com.google.inject.AbstractModule;

// TODO Document.
public abstract class RenderModule extends AbstractModule
{
    // TODO Document.
    private final ViewBinder end;
    
    // TODO Document.
    public RenderModule(ViewBinder viewBinder)
    {
        this.end = viewBinder;
    }
    
    // TODO Document.
    public ViewBinder end()
    {
        return end;
    }
}
