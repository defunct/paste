package com.goodworkalan.guicelet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

public class AnnotationsTest
{
    @Test
    public void any()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<Class<? extends Annotation>, Parameters> mapOfParameters = new HashMap<Class<? extends Annotation>, Parameters>();
        Annotations annotations = new Annotations(mapOfParameters, request);
        assertTrue(annotations.invoke(new String[] {}, "", new String[0]));
    }

    @Test
    public void on()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<Class<? extends Annotation>, Parameters> mapOfParameters = new HashMap<Class<? extends Annotation>, Parameters>();
        Parameters parameters = new Parameters();
        parameters.add("save", "Save");
        mapOfParameters.put(Binding.class, parameters);
        Annotations annotations = new Annotations(mapOfParameters, request);
        assertTrue(annotations.invoke(new String[] { "save" }, "", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "", new String[0]));
    }

    @Test
    public void param()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Map<Class<? extends Annotation>, Parameters> mapOfParameters = new HashMap<Class<? extends Annotation>, Parameters>();
        Parameters parameters = new Parameters();
        parameters.add("on", "save");
        mapOfParameters.put(Binding.class, parameters);
        Annotations annotations = new Annotations(mapOfParameters, request);
        assertTrue(annotations.invoke(new String[] { "save" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "save" }, "off", new String[0]));
    }

    @Test
    public void method()
    {
        Map<Class<? extends Annotation>, Parameters> mapOfParameters = new HashMap<Class<? extends Annotation>, Parameters>();
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getMethod()).thenReturn("POST");
        Annotations annotations = new Annotations(mapOfParameters, request);
        assertTrue(annotations.invoke(new String[0], "", new String[] { "POST" }));
        assertFalse(annotations.invoke(new String[0], "", new String[] { "GET" }));
    }
}
