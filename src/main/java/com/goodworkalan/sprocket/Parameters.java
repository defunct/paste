package com.goodworkalan.sprocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models URL name value parameters using Java collections and merges parameters
 * from different sources, such as request parameters and URL binding
 * parameters.
 * <p>
 * For a while, I considered providing overwrite as an alternative to merge, but
 * I couldn't figure out what it meant, especially when a set can define
 * multiple values for a name. In any case, the developer has enough control
 * over the URL structure that there really ought not be collisions between URL
 * binding parameters and request parameters.
 * 
 * @author Alan Gutierrez
 */
public class Parameters
extends StringListMap
{
    // TODO Document.
    public final static Object REQUEST = new Object();
    
    // TODO Document.
    public final static Object BINDING = new Object();
    
    // TODO Document.
    public Parameters()
    {
        super(new HashMap<String, List<String>>());
    }
    
    // TODO Document.
    private Parameters(Map<String, List<String>> parameters)
    {
        super(parameters);
    }

    // TODO Document.
    public final static Parameters fromStringArrayMap(Map<String, String[]> map)
    {
        Parameters parameters = new Parameters();
        for (String key : map.keySet())
        {
            for (String value : map.get(key))
            {
                parameters.add(key, value);
            }
        }
        return parameters;
    }
    
    // TODO Document.
    public final static Parameters fromStringMap(Map<String, String> map)
    {
        Parameters parameters = new Parameters();
        for (String key : map.keySet())
        {
            parameters.add(key, map.get(key));
        }
        return parameters;
    }

    // TODO Document.
    public static Parameters merge(Map<?, Parameters> parameters)
    {
        Parameters[] merge = parameters.values().toArray(new Parameters[parameters.size()]);
        return Parameters.merge(merge);
    }
    
    // TODO Document.
    public final static Parameters merge(Parameters...parameters)
    {
        Parameters merged = new Parameters();
        for (Parameters merge : parameters)
        {
            for (String key : merge.keySet())
            {
                if (!merged.containsKey(key))
                {
                    merged.put(key, new ArrayList<String>());
                }
                for (String value : merge.get(key))
                {
                    merged.add(key, value);
                }
            }
        }
        return merged;
    }
    
    public Parameters unmodifiable()
    {
        return new Parameters(unmodifiableMap());
    }
}
