package com.goodworkalan.paste;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import org.testng.annotations.Test;

import com.goodworkalan.paste.invoke.Invoke;

public class NestedTest {
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
    
    public class Nested {
        public int i;
        
        @Invoke
        public void action() {
        }
    }
}
