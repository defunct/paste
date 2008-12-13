package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class RequestScope implements Scope
{
    private final Map<Class<? extends Annotation>, Parameters> parameters;
    
    private final HttpServletRequest request;
    
    public RequestScope(Map<Class<? extends Annotation>, Parameters> parameters, HttpServletRequest request)
    {
        this.parameters = parameters;
        this.request = request;
    }

    private <T> Class<? extends Annotation> isAnnotatedParameters(Key<T> key, Object object)
    {
        if (object instanceof Parameters)
        {
            if (key.getAnnotationType() != null)
            {
                return key.getAnnotationType();
            }
            else if (key.getAnnotation() != null)
            {
                return key.getAnnotation().getClass();
            }
        }
        return null;
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped)
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
                        Class<? extends Annotation> annotationType = isAnnotatedParameters(key, t);
                        if (annotationType != null)
                        {
                            parameters.put(annotationType, (Parameters) t);
                        }
                        request.setAttribute(name, t);
                    }
                }
                return t;
            }
        };
    }
}
