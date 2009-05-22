package com.goodworkalan.paste.bean;

import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.PropertyPath;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Parameters;
import com.google.inject.Inject;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class BeanActor implements Actor
{
    // TODO Document.
    private final Parameters parameters;
    
    // TODO Document.
    @Inject
    public BeanActor(@Controller Parameters parameters)
    {
        this.parameters = parameters;
    }
    
    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        for (String key : parameters.getNames())
        {
            String value = parameters.getFirst(key);
            if (value != null)
            {
                PropertyPath path = null;
                try
                {
                    path = new PropertyPath(key);
                }
                catch (PathException e)
                {
                    continue;
                }
                try
                {
                    path.set(controller, value, true);
                }
                catch (PathException e)
                {
                }
            }
        }
        
        return null;
    }
}