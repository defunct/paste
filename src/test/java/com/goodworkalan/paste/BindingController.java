package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;

import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Inject;

public class BindingController
{
    @Inject
    public BindingController(@Controller Parameters parameters)
    {
        assertEquals(parameters.getFirst("user"), "alan");
    }
}
