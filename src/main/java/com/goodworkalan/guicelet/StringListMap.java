package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StringListMap
implements Map<String, List<String>>
{
    private final Map<String, List<String>> map;

    public StringListMap()
    {
        this(new HashMap<String, List<String>>());
    }

    public StringListMap(Map<String, List<String>> map)
    {
        this.map = map;
    }
    
    public Map<String, List<String>> unmodifiableMap()
    {
        Map<String, List<String>> unmodifiable = new HashMap<String, List<String>>();
        for (String key : map.keySet())
        {
            List<String> values = new ArrayList<String>(map.get(key));
            unmodifiable.put(key, Collections.unmodifiableList(values));
        }
        return Collections.unmodifiableMap(unmodifiable);
    }
    
    public String getFirst(String key)
    {
        List<String> values = map.get(key);
        if (values != null && values.size() != 0)
        {
            return values.get(0);
        }
        return null;
    }
    
    public void add(String key, String value)
    {
        List<String> values = map.get(key);
        if (values == null)
        {
            values = new ArrayList<String>();
            map.put(key, values);
        }
        values.add(value);
    }
    
    public List<String> put(String key, List<String> value)
    {
        return map.put(key, value);
    }
    
    public void putAll(Map<? extends String, ? extends List<String>> t)
    {
        map.putAll(t);
    }
    
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }
    
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }
    
    public List<String> get(Object key)
    {
        return map.get(key);
    }

    public Set<java.util.Map.Entry<String, List<String>>> entrySet()
    {
        return map.entrySet();
    }
    
    public Set<String> keySet()
    {
        return map.keySet();
    }
    
    public Collection<List<String>> values()
    {
        return map.values();
    }
    
    public int size()
    {
        return map.size();
    }
    
    public List<String> remove(Object key)
    {
        return map.remove(key);
    }
    
    public void clear()
    {
        map.clear();
    }
    
    public boolean isEmpty()
    {
        return map.isEmpty();
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
            return map.equals(object);
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return map.hashCode();
    }
}
