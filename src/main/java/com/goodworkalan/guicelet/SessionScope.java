package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class SessionScope implements Scope
{
    private final HttpServletRequest request;
    
    public SessionScope(HttpServletRequest request)
    {
        this.request = request;
    }

    public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            HttpSession session = request.getSession();
            public T get()
            {
                @SuppressWarnings("unchecked")
                T t = (T) session.getAttribute(name);
                if (t == null)
                {
                    t = unscoped.get();
                    session.setAttribute(name, t);
                }
                return t;
            }
        };
    }
}
