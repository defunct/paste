package com.goodworkalan.paste;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.PathException;

// TODO Document.
public class Evaluator
{
    // TODO Document.
    private final Object controller;
    
    // TODO Document.
    public Evaluator(Object controller)
    {
        this.controller = controller;
    }

    // TODO Document.
    public String get(String expression)
    {
        try
        {
            Diffusion diffusion = new Diffusion(controller);
            Object object = diffusion.get(expression);
            return object == null ? null : object.toString();
        }
        catch (PathException e)
        {
            throw new PasteException(e);
        }
    }
}
