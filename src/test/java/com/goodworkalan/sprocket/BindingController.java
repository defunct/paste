package com.goodworkalan.sprocket;

import static org.testng.Assert.assertEquals;

import com.goodworkalan.sprocket.Binding;
import com.goodworkalan.sprocket.Parameters;
import com.google.inject.Inject;

public class BindingController
{
    @Inject
    public BindingController(@Binding Parameters parameters)
    {
        assertEquals(parameters.getFirst("user"), "alan");
    }
}
