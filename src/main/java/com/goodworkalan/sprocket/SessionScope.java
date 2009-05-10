package com.goodworkalan.sprocket;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Implements HTTP servlet request session scope.
 * 
 * @author Alan Gutierrez
 */
public class SessionScope implements Scope
{
    /** Thread local storage for the HTTP servlet request. */
    private final static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
    
    /**
     * Enter the scope by setting the HTTP request that manages the session.
     * 
     * @param request The HTTP servlet request.
     */
    public void enter(HttpServletRequest request)
    {
        SessionScope.request.set(request);
    }
    
    /**
     * Leave the scope by removing the HTTP request that manages the session.
     */
    public void exit()
    {
        request.remove();
    }

    /**
     * Scopes a provider in the the HTTP request session. The returned provider
     * returns objects from this scope. If an object does not exist in this
     * scope, the given unscoped provider is used to retrieve one.
     * 
     * @param key
     *            The binding key.
     * @param unscoped
     *            Use to locate an instance if one does not already exist in the
     *            scope.
     * @return A new provider which only delegates to the given unscoped
     *         provider when an instance of the requested object doesn't already
     *         exist in this scope.
     */
    public <T> Provider<T> scope(Key<T> key, final Provider<T> unscoped)
    {
        final String name = key.toString();
        return new Provider<T>()
        {
            public T get()
            {
                HttpSession session = request.get().getSession();

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
