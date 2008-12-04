package com.goodworkalan.guicelet.bean;

import java.util.Map;

import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.RequestParameters;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class BeanActor implements Actor
{
    private final Map<String, String[]> parameters;
    
    public BeanActor(@RequestParameters Map<String, String[]> parameters)
    {
        this.parameters = parameters;
    }
    
    public void actUpon(Object controller)
    {
        for (String name : parameters.keySet())
        {
            String[] value = parameters.get(name);
            if (value != null && value.length != 0)
            {
                PropertyPath path = null;
                try
                {
                    path = new PropertyPath(name);
                }
                catch (PropertyPath.Error e)
                {
                    continue;
                }
                try
                {
                    path.set(controller, value[0]);
                }
                catch (PropertyPath.Error e)
                {
                }
            }
        }
    }

}
