package com.goodworkalan.guicelet.validation;

import java.util.Map;

import com.goodworkalan.guicelet.Actor;

public class ValidationActor implements Actor
{
    private final Map<String, String> errors ;
    
    public ValidationActor(@Errors Map<String, String> errors)
    {
        this.errors = errors;
    }

    public void actUpon(Object controller)
    {
        if (errors.size() != 0)
        {
            throw new Invalid();
        }
    }
}