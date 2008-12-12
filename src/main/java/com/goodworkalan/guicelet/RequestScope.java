package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class RequestScope implements Scope
{
    private final HttpServletRequest request;
    
    public RequestScope(HttpServletRequest request)
    {
        this.request = request;
    }

    public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            @SuppressWarnings("unchecked")
            public T get()
            {
                T t;
                synchronized (request)
                {
                    t = (T) request.getAttribute(name);
                    if (t == null)
                    {
                        t = unscoped.get();
                        request.setAttribute(name, t);
                    }
                }
                return t;
            }
        };
    }
}
