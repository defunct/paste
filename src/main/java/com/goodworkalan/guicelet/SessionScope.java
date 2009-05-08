package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

// TODO Document.
public class SessionScope implements Scope
{
    // TODO Document.
    private final static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
    
    // TODO Document.
    public static void set(HttpServletRequest value)
    {
        request.set(value);
    }

    // TODO Document.
    public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            HttpSession session = request.get().getSession();
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
