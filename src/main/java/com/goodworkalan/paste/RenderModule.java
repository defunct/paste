package com.goodworkalan.paste;

import com.google.inject.AbstractModule;

// TODO Document.
public abstract class RenderModule extends AbstractModule
{
    // TODO Document.
    private final RenderStatement end;
    
    // TODO Document.
    public RenderModule(RenderStatement end)
    {
        this.end = end;
    }
    
    // TODO Document.
    public RenderStatement end()
    {
        return end;
    }
}
