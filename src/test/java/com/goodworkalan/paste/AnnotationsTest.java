package com.goodworkalan.paste;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.paste.controller.Annotations;
import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.Parameters;

// TODO Document.
public class AnnotationsTest {
    @Test
    public void any() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Annotations annotations = new Annotations(new Parameters(), request);
        assertTrue(annotations.invoke(new String[] {}, "", new String[0]));
    }

    // TODO Document.
    @Test
    public void on() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Parameters parameters = new Parameters();
        parameters.add(new NamedValue("save", "Save"));
        Annotations annotations = new Annotations(new Parameters(parameters), request);
        assertTrue(annotations.invoke(new String[] { "save" }, "", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "", new String[0]));
    }

    // TODO Document.
    @Test
    public void param() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Parameters parameters = new Parameters();
        parameters.add(new NamedValue("on", "save"));
        Annotations annotations = new Annotations(new Parameters(parameters), request);
        assertTrue(annotations.invoke(new String[] { "save" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "save" }, "off", new String[0]));
    }

    // TODO Document.
    @Test
    public void method() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        Annotations annotations = new Annotations(new Parameters(), request);
        assertTrue(annotations.invoke(new String[0], "", new String[] { "POST" }));
        assertFalse(annotations.invoke(new String[0], "", new String[] { "GET" }));
    }
}
