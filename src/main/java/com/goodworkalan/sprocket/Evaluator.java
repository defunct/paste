package com.goodworkalan.sprocket;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;

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
            Object object = property.get(controller, false);
            return object == null ? null : object.toString();
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
    }
}
