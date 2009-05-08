package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// TODO Document.
public class StringListMap
implements Map<String, List<String>>
{
    // TODO Document.
    private final Map<String, List<String>> map;

    // TODO Document.
    public StringListMap()
    {
        this(new HashMap<String, List<String>>());
    }

    // TODO Document.
    public StringListMap(Map<String, List<String>> map)
    {
        this.map = map;
    }
    
    // TODO Document.
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
    
    // TODO Document.
    public String getFirst(String key)
    {
        List<String> values = map.get(key);
        if (values != null && values.size() != 0)
        {
            return values.get(0);
        }
        return null;
    }
    
    // TODO Document.
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
    
    // TODO Document.
    public List<String> put(String key, List<String> value)
    {
        return map.put(key, value);
    }
    
    // TODO Document.
    public void putAll(Map<? extends String, ? extends List<String>> t)
    {
        map.putAll(t);
    }
    
    // TODO Document.
    public boolean containsKey(Object key)
    {
        return map.containsKey(key);
    }
    
    // TODO Document.
    public boolean containsValue(Object value)
    {
        return map.containsValue(value);
    }
    
    // TODO Document.
    public List<String> get(Object key)
    {
        return map.get(key);
    }

    // TODO Document.
    public Set<java.util.Map.Entry<String, List<String>>> entrySet()
    {
        return map.entrySet();
    }
    
    // TODO Document.
    public Set<String> keySet()
    {
        return map.keySet();
    }
    
    // TODO Document.
    public Collection<List<String>> values()
    {
        return map.values();
    }
    
    // TODO Document.
    public int size()
    {
        return map.size();
    }
    
    // TODO Document.
    public List<String> remove(Object key)
    {
        return map.remove(key);
    }
    
    // TODO Document.
    public void clear()
    {
        map.clear();
    }
    
    // TODO Document.
    public boolean isEmpty()
    {
        return map.isEmpty();
    }
    
    // TODO Document.
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
    
    // TODO Document.
    @Override
    public int hashCode()
    {
        return map.hashCode();
    }
    
    // TODO Document.
    @Override
    public String toString()
    {
        return map.toString();
    }
}
