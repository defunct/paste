package com.goodworkalan.guicelet.validation;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

public class ValidationActorTest
{
    @Test(expectedExceptions=Invalid.class)
    public void invalid()
    {
        Map<String, String> errors = new HashMap<String, String>();
        errors.put("hello", "Error!");
        new ValidationActor(errors).actUpon(new Object());
    }
    
    @Test
    public void valid()
    {
        Map<String, String> errors = new HashMap<String, String>();
        new ValidationActor(errors).actUpon(new Object());
    }
}
