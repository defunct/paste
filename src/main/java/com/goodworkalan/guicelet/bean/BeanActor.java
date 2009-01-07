package com.goodworkalan.guicelet.bean;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.ParametersServer;
import com.google.inject.Inject;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class BeanActor implements Actor
{
    private final ParametersServer parameters;
    
    @Inject
    public BeanActor(ParametersServer parameters)
    {
        this.parameters = parameters;
    }
    
    public void actUpon(Object controller)
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
    }
}