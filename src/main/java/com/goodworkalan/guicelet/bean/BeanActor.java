package com.goodworkalan.guicelet.bean;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Parameters;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class BeanActor implements Actor
{
    private final Map<Class<? extends Annotation>, Parameters> parameters;
    
    public BeanActor(Map<Class<? extends Annotation>, Parameters> parameters)
    {
        this.parameters = parameters;
    }
    
    public void actUpon(Object controller)
    {
        Parameters[] merge = new Parameters[parameters.size()];
        int index = 0;
        for (Class<? extends Annotation> key : parameters.keySet())
        {
            merge[index++] = parameters.get(key);
        }            
        
        Parameters merged = Parameters.merge(merge);
        
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
                catch (PropertyPath.Error e)
                {
                    continue;
                }
                try
                {
                    path.set(controller, value, true);
                }
                catch (PropertyPath.Error e)
                {
                }
            }
        }
    }
}