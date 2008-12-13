package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringListMap
{
    private final Map<String, List<String>> parameters;

    public StringListMap()
    {
        this(new HashMap<String, List<String>>());
    }

    public StringListMap(Map<String, List<String>> map)
    {
        this.parameters = map;
    }
    
    public Map<String, List<String>> unmodifiableMap()
    {
        Map<String, List<String>> unmodifiable = new HashMap<String, List<String>>();
        for (String key : parameters.keySet())
        {
            List<String> values = new ArrayList<String>(parameters.get(key));
            unmodifiable.put(key, Collections.unmodifiableList(values));
        }
        return Collections.unmodifiableMap(unmodifiable);
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
