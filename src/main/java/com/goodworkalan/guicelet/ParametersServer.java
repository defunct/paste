package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class ParametersServer
{
    private final Map<Key, Parameters> values = new LinkedHashMap<Key, Parameters>();
    
    private final List<Key> order = new ArrayList<Key>();
    
    public ParametersServer()
    {
    }
    
    public Parameters get(Object key)
    {
        Key boxed = new Key(key);
        Parameters parameters = values.get(boxed);
        if (parameters == null)
        {
            parameters = new Parameters();
            values.put(boxed, parameters);
            order.add(boxed);
        }
        return parameters;
    }
    
    public Parameters merge()
    {
        return merge(Parameters.BINDING, Parameters.REQUEST);
    }
    
    public Parameters merge(Object...keys)
    {
        HashSet<Key> seen = new HashSet<Key>();
        Parameters[] parameters = new Parameters[values.size()];
        int parameter = 0;
        for (Object object : keys)
        {
            Key key = new Key(object);
            if (values.containsKey(key) && !seen.contains(key))
            {
                seen.add(key);
                parameters[parameter++] = values.get(key);
            }
        }
        for (Key key : order)
        {
            if (values.containsKey(key) && !seen.contains(key))
            {
                seen.add(key);
                parameters[parameter++] = values.get(key);
            }
        }
        return Parameters.merge(parameters);
    }
    
    private final static class Key
    {
        private final Object object;
        
        public Key(Object object)
        {
            this.object = object;
        }
        
        @Override
        public boolean equals(Object that)
        {
            if (that instanceof Key)
            {
                return object == ((Key) that).object;
            }
            return false;
        }
        
        @Override
        public int hashCode()
        {
            return System.identityHashCode(object);
        }
    }
}