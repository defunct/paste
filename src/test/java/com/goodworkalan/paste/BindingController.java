package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;

import javax.inject.Inject;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Controller;

// TODO Document.
public class BindingController {
    // TODO Document.
    @Inject
    public BindingController(@Controller Parameters parameters) {
        assertEquals(parameters.get("user"), "alan");
    }
}
