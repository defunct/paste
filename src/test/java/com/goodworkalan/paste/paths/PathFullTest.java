package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

// TODO Document.
public class PathFullTest extends FormatTest {
    // TODO Document.
    @Test
    public void formatFullPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s", args(PathFull.class)), "/path/file.html");
    }
}
