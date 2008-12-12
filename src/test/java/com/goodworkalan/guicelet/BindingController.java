package com.goodworkalan.guicelet;

import static org.testng.Assert.assertEquals;

import com.google.inject.Inject;

public class BindingController
{
    @Inject
    public BindingController(@Binding Parameters parameters)
    {
        assertEquals(parameters.getFirst("user"), "alan");
    }
}
