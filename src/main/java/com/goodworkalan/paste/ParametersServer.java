package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// TODO Document.
@RequestScoped
public class ParametersServer
{
    // TODO Document.
    private final Map<Key, Parameters> values = new LinkedHashMap<Key, Parameters>();
    
    // TODO Document.
    private final List<Key> order = new ArrayList<Key>();
    
    // TODO Document.
    public ParametersServer()
    {
    }
    
    // TODO Document.
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
    
    // TODO Document.
    public Parameters merge()
    {
        return merge(Parameters.BINDING, Parameters.REQUEST);
    }
    
    // TODO Document.
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
    
    // TODO Document.
    private final static class Key
    {
        // TODO Document.
        private final Object object;
        
        // TODO Document.
        public Key(Object object)
        {
            this.object = object;
        }
        
        // TODO Document.
        @Override
        public boolean equals(Object that)
        {
            if (that instanceof Key)
            {
                return object == ((Key) that).object;
            }
            return false;
        }
        
        // TODO Document.
        @Override
        public int hashCode()
        {
            return System.identityHashCode(object);
        }
    }
}
