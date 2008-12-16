package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public interface Validator
{
    public void validate(Type type, Annotation annotation);
    
    public void validate(Field field, Annotation annotation);

    public void validate(Method method, Annotation annotation);
}