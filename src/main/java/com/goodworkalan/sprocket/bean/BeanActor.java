package com.goodworkalan.sprocket.bean;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.sprocket.Actor;
import com.goodworkalan.sprocket.Parameters;
import com.goodworkalan.sprocket.ParametersServer;
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