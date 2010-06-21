package com.goodworkalan.paste;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import org.testng.annotations.Test;

import com.goodworkalan.paste.invoke.Invoke;

// TODO Document.
public class NestedTest {
    // TODO Document.
    @Test
    public void nested() {
        Class<?> nestedClass = Nested.class;
        System.out.println(nestedClass.getCanonicalName());
        System.out.println(nestedClass.getDeclaringClass().getCanonicalName());
        for (Constructor<?> constructor : nestedClass.getConstructors()) {
            for (Type type : constructor.getParameterTypes()) {
                System.out.println(type);
            }
        }
    }
    
    // TODO Document.
    public class Nested {
        // TODO Document.
        public int i;
        
        // TODO Document.
        @Invoke
        public void action() {
        }
    }
}
