package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link ControllerClassAsPath} class.
 * 
 * @author Alan Gutierrez
 */
public class ControllerPackageAsPathTest extends FormatTest {
    /** Test injector construction. */
    @Test
    public void format() {
        PathFormatter formatter = new PathFormatter(getControllerInjector());
        assertEquals(formatter.format("/%s.ftl", ControllerPackageAsPath.class), "/java/lang.ftl");
    }
}
