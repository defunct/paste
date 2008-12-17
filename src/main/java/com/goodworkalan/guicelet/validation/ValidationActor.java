package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.goodworkalan.guicelet.Actor;
import com.google.inject.Injector;

public class ValidationActor implements Actor
{
    private final Injector injector;
    
    private final Map<String, String> errors ;
    
    public ValidationActor(Injector injector, @Faults Map<String, String> errors)
    {
        this.injector = injector;
        this.errors = errors;
    }

    private void validate(Method method, Validation validation, Object controller)
    {
        Validator validator = (Validator) injector.getInstance(validation.validator());
        validator.validate(method, validation);
    }

    public void actUpon(Object controller)
    {
        for (Method method : controller.getClass().getMethods())
        {
            for (Annotation annotation : method.getAnnotations())
            {
                for (Annotation subAnnotation : annotation.getClass().getAnnotations())
                {
                    if (subAnnotation.annotationType().equals(Validation.class))
                    {
                        validate(method, annotation.getClass().getAnnotation(Validation.class), controller);
                    }
                }
            }
        }
        if (errors.size() != 0)
        {
            throw new Invalid();
        }
    }
}