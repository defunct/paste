package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;

import javax.inject.Inject;

import com.goodworkalan.paste.util.Parameters;

public class BindingController
{
    @Inject
    public BindingController(@Controller Parameters parameters)
    {
        assertEquals(parameters.getFirst("user"), "alan");
    }
}
