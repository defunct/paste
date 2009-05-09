package com.goodworkalan.sprocket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.sprocket.Annotations;
import com.goodworkalan.sprocket.Parameters;
import com.goodworkalan.sprocket.ParametersServer;

public class AnnotationsTest
{
    @Test
    public void any()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Annotations annotations = new Annotations(new ParametersServer(), request);
        assertTrue(annotations.invoke(new String[] {}, "", new String[0]));
    }

    @Test
    public void on()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ParametersServer parameters = new ParametersServer();
        parameters.get(Parameters.BINDING).add("save", "Save");
        Annotations annotations = new Annotations(parameters, request);
        assertTrue(annotations.invoke(new String[] { "save" }, "", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "", new String[0]));
    }

    @Test
    public void param()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ParametersServer parameters = new ParametersServer();
        parameters.get(Parameters.BINDING).add("on", "save");
        Annotations annotations = new Annotations(parameters, request);
        assertTrue(annotations.invoke(new String[] { "save" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "save" }, "off", new String[0]));
    }

    @Test
    public void method()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("POST");
        Annotations annotations = new Annotations(new ParametersServer(), request);
        assertTrue(annotations.invoke(new String[0], "", new String[] { "POST" }));
        assertFalse(annotations.invoke(new String[0], "", new String[] { "GET" }));
    }
}
