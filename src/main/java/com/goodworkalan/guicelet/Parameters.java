package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
implements Map<String, List<String>>
{
    private final Map<String, List<String>> parameters;
    
    public Parameters()
    {
        this.parameters = new HashMap<String, List<String>>();
    }
    
    private Parameters(Map<String, List<String>> parameters)
    {
        this.parameters = parameters;
    }

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
    
    public final static Parameters fromStringMap(Map<String, String> map)
    {
        Parameters parameters = new Parameters();
        for (String key : map.keySet())
        {
            parameters.add(key, map.get(key));
        }
        return parameters;
    }
    
    public final static Parameters merge(Parameters...parameters)
    {
        Parameters merged = new Parameters();
        for (Parameters merge : parameters)
        {
            for (String key : merge.keySet())
            {
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
        Map<String, List<String>> unmodifiable = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> parameter : parameters.entrySet())
        {
            unmodifiable.put(parameter.getKey(), Collections.unmodifiableList(new ArrayList<String>(parameter.getValue())));
        }
        return new Parameters(Collections.unmodifiableMap(unmodifiable));
    }
    
    public String getFirst(String key)
    {
        List<String> values = parameters.get(key);
        if (values != null && values.size() != 0)
        {
            return values.get(0);
        }
        return null;
    }
    
    public void add(String key, String value)
    {
        List<String> values = parameters.get(key);
        if (values == null)
        {
            values = new ArrayList<String>();
            parameters.put(key, values);
        }
        values.add(value);
    }
    
    public List<String> put(String key, List<String> value)
    {
        return parameters.put(key, value);
    }
    
    public void putAll(Map<? extends String, ? extends List<String>> t)
    {
        parameters.putAll(t);
    }
    
    public boolean containsKey(Object key)
    {
        return parameters.containsKey(key);
    }
    
    public boolean containsValue(Object value)
    {
        return parameters.containsValue(value);
    }
    
    public List<String> get(Object key)
    {
        return parameters.get(key);
    }

    public Set<java.util.Map.Entry<String, List<String>>> entrySet()
    {
        return parameters.entrySet();
    }
    
    public Set<String> keySet()
    {
        return parameters.keySet();
    }
    
    public Collection<List<String>> values()
    {
        return parameters.values();
    }
    
    public int size()
    {
        return parameters.size();
    }
    
    public List<String> remove(Object key)
    {
        return parameters.remove(key);
    }
    
    public void clear()
    {
        parameters.clear();
    }
    
    public boolean isEmpty()
    {
        return parameters.isEmpty();
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object == this)
        {
            return true;
        }
        if (object instanceof Map)
        {
            return parameters.equals(object);
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return parameters.hashCode();
    }
}
