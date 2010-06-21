package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

// TODO Document.
public class ControllerClassNameTest extends FormatTest {
    // TODO Document.
    @Test
    public void format() {
        PathFormatter formatter = new PathFormatter(getControllerInjector());
        assertEquals(formatter.format("/%s.ftl", args(ControllerClassName.class)), "/Object.ftl");
    }
}
