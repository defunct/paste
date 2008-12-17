package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.regex.Pattern;

public abstract class AbstractValidator implements Validator
{
    private final static Pattern THIS = Pattern.compile("^\\s*(this)[^\\W\\D]");   
        
    protected abstract void validate(Annotation annotation, PathFixup fixup, String context);
    
    public void validate(Method method, Annotation annotation)
    {
        // Determine the bean property name.
        String name = method.getName();
        int start = 0;
        if (name.startsWith("is"))
        {
            start = 2;
        }
        if (name.startsWith("get") || name.startsWith("set"))
        {
            start = 3;
        }
        name = name.substring(start, start + 1).toLowerCase() + name.substring(start + 1);
        
        // Create the root context.
        String context = method.getDeclaringClass().getCanonicalName() + '.' + method.getName();
        
        // Call validate.
        validate(annotation, new PathFixup(THIS, name), context);
    }

    public void validate(Type type, Annotation annotation)
    {
    }

    public void validate(Field field, Annotation annotation)
    {
    }
}
