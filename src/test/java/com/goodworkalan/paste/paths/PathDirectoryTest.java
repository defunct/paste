package com.goodworkalan.paste.paths;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

// TODO Document.
public class PathDirectoryTest extends FormatTest {
    // TODO Document.
    @Test
    public void formatDirectory() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s/welcome.ftl", args(PathDirectory.class)), "/path/welcome.ftl");
    }
    
    // TODO Document.
    @Test
    public void formatEmptyPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector(""));
        assertEquals(formatter.format("%s/welcome.ftl", args(PathDirectory.class)), "/welcome.ftl");
    }
}
