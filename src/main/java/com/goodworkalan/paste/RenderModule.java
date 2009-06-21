package com.goodworkalan.paste;

import com.google.inject.AbstractModule;

// TODO Document.
public abstract class RenderModule extends AbstractModule
{
    // TODO Document.
    private final ViewConnector end;
    
    // TODO Document.
    public RenderModule(ViewConnector end)
    {
        this.end = end;
    }
    
    // TODO Document.
    public ViewConnector end()
    {
        return end;
    }
}
