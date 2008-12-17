package com.goodworkalan.guicelet;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class ControllerScope implements Scope
{
    private final Map<Object, Object> scope = new HashMap<Object, Object>();
    
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped)
    {
        return new Provider<T>()
        {
            @SuppressWarnings("unchecked")
            public T get()
            {
                T t = null;
                synchronized (scope)
                {
                    if (scope.containsKey(key))
                    {
                        t = (T) scope.get(key);
                    }
                    else
                    {
                        t = unscoped.get();
                        scope.put(key, t);
                    }
                }
                return t;
            }
        };
    }
}
