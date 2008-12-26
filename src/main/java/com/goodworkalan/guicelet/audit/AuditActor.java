package com.goodworkalan.guicelet.audit;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Parameters;

public class AuditActor implements Actor
{
    private final Map<Class<? extends Annotation>, Parameters> parameters;
    
    public AuditActor(Map<Class<? extends Annotation>, Parameters> parameters)
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
        
        Map<Object, Object> map = new HashMap<Object, Object>();
        
        for (String key : merged.keySet())
        {
            String value = merged.getFirst(key);
            
            PropertyPath path;
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
                path.set(map, value, true);
            }
            catch (PathException e)
            {
                continue;
            }
        }
        
        
    }
}
