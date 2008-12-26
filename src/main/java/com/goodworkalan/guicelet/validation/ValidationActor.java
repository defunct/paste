package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Actors;
import com.google.inject.Injector;

public class ValidationActor implements Actor
{
    private final Injector injector;
    
    private final Map<String, String> faults ;
    
    public ValidationActor(Injector injector, @Faults Map<String, String> errors)
    {
        this.injector = injector;
        this.faults = errors;
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
        if (faults.size() != 0)
        {
            boolean raise = true;
            Actors actors = controller.getClass().getAnnotation(Actors.class);
            for (Class<? extends Actor> actor : actors.value())
            {
                if (actor.equals(RaiseInvalidActor.class))
                {
                    raise = false;
                }
            }
            if (raise)
            {
                throw new Invalid();
            }
        }
    }
}