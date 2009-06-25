package com.goodworkalan.paste;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.dovetail.Glob;

// TODO Document.
public class Routes
{
    // TODO Document.
    private final Map<Class<?>, Glob> controllerToGlob;
    
    // TODO Document.
    public Routes(Map<Class<?>, Glob> controllerToGlob)
    {
        this.controllerToGlob = controllerToGlob;
    }
    
    // TODO Document.
    public String path(Class<?> controllerClass)
    {
        return controllerToGlob.get(controllerClass).path(Collections.<String, String>emptyMap());
    }
    
    // TODO Document.
    public String path(Class<?> controllerClass, Map<String, String> parameters)
    {
        return controllerToGlob.get(controllerClass).path(parameters);
    }

    // TODO Document.
    public String path(Class<?> controllerClass, Object...parameters)
    {
        if (parameters.length % 2 != 0)
        {
            throw new IllegalArgumentException();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < parameters.length; i++)
        {
            map.put(parameters[i].toString(), parameters[i + 1].toString());
        }
        return controllerToGlob.get(controllerClass).path(map);
    }
}
