package com.goodworkalan.paste.paths;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link PathDirectory} class.
 * 
 * @author Alan Gutierrez
 */
public class PathDirectoryTest extends FormatTest {
    /** Test a path format. */
    @Test
    public void formatDirectory() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s/welcome.ftl", PathDirectory.class), "/path/welcome.ftl");
    }
    
    /** Test a path format with an empty string value for tpath. */
    @Test
    public void formatEmptyPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector(""));
        assertEquals(formatter.format("%s/welcome.ftl", PathDirectory.class), "/welcome.ftl");
    }
}
