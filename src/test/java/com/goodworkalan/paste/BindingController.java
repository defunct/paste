package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;

import com.goodworkalan.paste.Binding;
import com.goodworkalan.paste.Parameters;
import com.google.inject.Inject;

public class BindingController
{
    @Inject
    public BindingController(@Binding Parameters parameters)
    {
        assertEquals(parameters.getFirst("user"), "alan");
    }
}
