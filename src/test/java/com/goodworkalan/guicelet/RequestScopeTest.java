package com.goodworkalan.guicelet;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertSame;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.google.inject.Key;
import com.google.inject.Provider;

public class RequestScopeTest
{
    @Test
    public void get()
    {
        final Map<Object, Object> map = new HashMap<Object, Object>();

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute(anyString())).thenAnswer(new Answer<Object>()
                {
                    public Object answer(InvocationOnMock invocation)
                            throws Throwable
                    {
                        String key = (String) invocation.getArguments()[0];
                        return map.get(key);
                    }
                });
        doAnswer(new Answer<Void>()
                {
                    public Void answer(InvocationOnMock invocation)
                            throws Throwable
                    {
                        Object[] arguments = invocation.getArguments();
                        map.put(arguments[0], arguments[1]);
                        return null;
                    }
                }).when(request).setAttribute(anyString(), anyObject());
        Provider<Object> provider = new Provider<Object>()
        {
            public Object get()
            {
                return new Object();
            }
        };
        Provider<Parameters> parameters = new Provider<Parameters>()
        {
            public Parameters get()
            {
                return new Parameters();
            }
        };
        Bindable requestable = new Bindable();
        RequestScope scope = new RequestScope(new HashMap<Class<? extends Annotation>, Parameters>(), request);
        Object object = scope.scope(Key.get(Object.class, Request.class), provider).get();
        assertSame(scope.scope(Key.get(Object.class, Request.class), provider).get(), object);
        scope.scope(Key.get(Parameters.class, Request.class), parameters).get();
        Binding binding = requestable.getClass().getAnnotation(Binding.class);
        scope.scope(Key.get(Parameters.class, binding), parameters).get();
    }
}
