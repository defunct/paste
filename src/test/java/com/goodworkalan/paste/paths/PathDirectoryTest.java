package com.goodworkalan.paste.paths;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class PathDirectoryTest extends FormatTest {
    @Test
    public void formatDirectory() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s/welcome.ftl", args(PathDirectory.class)), "/path/welcome.ftl");
    }
    
    @Test
    public void formatEmptyPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector(""));
        assertEquals(formatter.format("%s/welcome.ftl", args(PathDirectory.class)), "/welcome.ftl");
    }
}
