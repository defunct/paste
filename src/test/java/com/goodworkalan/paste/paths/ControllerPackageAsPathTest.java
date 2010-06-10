package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ControllerPackageAsPathTest extends FormatTest {
    @Test
    public void format() {
        PathFormatter formatter = new PathFormatter(getControllerInjector());
        assertEquals(formatter.format("/%s.ftl", args(ControllerPackageAsPath.class)), "/java/lang.ftl");
    }
}