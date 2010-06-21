package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

// TODO Document.
public class PathFileTest extends FormatTest {
    // TODO Document.
    @Test
    public void formatFileTest() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html", "index.html"));
        assertEquals(formatter.format("/new/%s", args(PathFile.class)), "/new/file.html");
    }

    // TODO Document.
    @Test
    public void formatWelcomeFile() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/", "index.html"));
        assertEquals(formatter.format("/new/%s", args(PathFile.class)), "/new/index.html");
    }
    
    // TODO Document.
    @Test
    public void formatEmptyPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector("", "index.html"));
        assertEquals(formatter.format("/new/%s", args(PathFile.class)), "/new/index.html");
    }
}
