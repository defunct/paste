package com.goodworkalan.paste;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.goodworkalan.paste.controller.Annotations;
import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.Parameters;

/**
 * Unit tests for the {@link Annotations} class.
 *
 * @author Alan Gutierrez
 */
public class AnnotationsTest {
    /** Test universal invocation. */
    @Test
    public void any() {
        Annotations annotations = new Annotations(new Parameters(), "GET");
        assertTrue(annotations.invoke(new String[] {}, "", new String[0]));
    }

    /** Test parameter name invocation. */
    @Test
    public void on() {
        Parameters parameters = new Parameters();
        parameters.add(new NamedValue("save", "Save"));
        Annotations annotations = new Annotations(new Parameters(parameters), "GET");
        assertTrue(annotations.invoke(new String[] { "save" }, "", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "", new String[0]));
    }

    /** Test parameter invocation. */
    @Test
    public void param() {
        Parameters parameters = new Parameters();
        parameters.add(new NamedValue("on", "save"));
        Annotations annotations = new Annotations(new Parameters(parameters), "GET");
        assertTrue(annotations.invoke(new String[] { "save" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "cancel" }, "on", new String[0]));
        assertFalse(annotations.invoke(new String[] { "save" }, "off", new String[0]));
    }

    /** Test request method invocation. */
    @Test
    public void method() {
       Annotations annotations = new Annotations(new Parameters(), "POST");
        assertTrue(annotations.invoke(new String[0], "", new String[] { "POST" }));
        assertFalse(annotations.invoke(new String[0], "", new String[] { "GET" }));
    }
}
