package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PathFullTest extends FormatTest {
    @Test
    public void formatFullPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s", args(FullPath.class)), "/path/file.html");
    }
}
