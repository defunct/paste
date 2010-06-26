package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link ControllerClassAsPath} class.
 * 
 * @author Alan Gutierrez
 */
public class ControllerClassAsPathTest extends FormatTest {
    /** Test injector construction. */
    @Test
    public void format() {
        PathFormatter formatter = new PathFormatter(getControllerInjector());
        assertEquals(formatter.format("/%s.ftl", ControllerClassAsPath.class), "/java/lang/Object.ftl");
    }
}
