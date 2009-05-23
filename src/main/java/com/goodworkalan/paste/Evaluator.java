package com.goodworkalan.paste;

import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.PropertyPath;

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
            PropertyPath property = new PropertyPath(expression);
            Object object = property.get(controller);
            return object == null ? null : object.toString();
        }
        catch (PathException e)
        {
            throw new PasteException(e);
        }
    }
}
