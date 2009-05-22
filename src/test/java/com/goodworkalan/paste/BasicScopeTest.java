package com.goodworkalan.paste;

import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

import com.goodworkalan.paste.BasicScope;
import com.goodworkalan.paste.Binding;
import com.goodworkalan.paste.Parameters;
import com.goodworkalan.paste.Request;
import com.google.inject.Key;
import com.google.inject.Provider;

public class BasicScopeTest
{
    @Test
    public void get()
    {
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
        BasicScope scope = new BasicScope();
        scope.enter();
        Object object = scope.scope(Key.get(Object.class, Request.class), provider).get();
        assertSame(scope.scope(Key.get(Object.class, Request.class), provider).get(), object);
        scope.scope(Key.get(Parameters.class, Request.class), parameters).get();
        Binding binding = requestable.getClass().getAnnotation(Binding.class);
        scope.scope(Key.get(Parameters.class, binding), parameters).get();
        scope.exit();
    }
}
