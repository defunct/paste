package com.goodworkalan.guicelet;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;

public class Evaluator
{
    private final Object controller;
    
    public Evaluator(Object controller)
    {
        this.controller = controller;
    }

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
