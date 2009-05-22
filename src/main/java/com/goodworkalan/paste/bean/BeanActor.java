package com.goodworkalan.paste.bean;

import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.PropertyPath;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Parameters;
import com.goodworkalan.paste.ParametersServer;
import com.google.inject.Inject;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class BeanActor implements Actor
{
    // TODO Document.
    private final ParametersServer parameters;
    
    // TODO Document.
    @Inject
    public BeanActor(ParametersServer parameters)
    {
        this.parameters = parameters;
    }
    
    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        Parameters merged = parameters.merge();
        
        for (String key : merged.keySet())
        {
            String value = merged.getFirst(key);
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