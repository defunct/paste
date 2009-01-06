package com.goodworkalan.guicelet;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class BasicScope implements Scope
{
    private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<Map<Key<?>, Object>>();

    public BasicScope()
    {
    }
    
    public void enter()
    {
        values.set(new HashMap<Key<?>, Object>());
    }

    public void exit()
    {
        values.remove();
    }
    
    public <T> void seed(Key<T> key, T value)
    {
        Map<Key<?>, Object> objects = values.get();
        objects.put(key, value);
    }
    
    public <T> void seed(Class<T> clazz, T value)
    {
        seed(Key.get(clazz), value);
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped)
    {
        return new Provider<T>()
        {
            public T get()
            {
                Map<Key<?>, Object> objects = values.get();

                @SuppressWarnings("unchecked")
                T current = (T) objects.get(key);
                if (current == null && !objects.containsKey(key))
                {
                    current = unscoped.get();
                    objects.put(key, current);
                }
                return current;
            }
        };
    }
}
