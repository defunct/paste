package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link PathFile} class.
 *
 * @author Alan Gutierrez
 */
public class PathFileTest extends FormatTest {
    /** Test the request path file format argument. */
    @Test
    public void formatFileTest() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html", "index.html"));
        assertEquals(formatter.format("/new/%s", PathFile.class), "/new/file.html");
    }

    /** Test the request path file format argument with a welcome file. */
    @Test
    public void formatWelcomeFile() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/", "index.html"));
        assertEquals(formatter.format("/new/%s", PathFile.class), "/new/index.html");
    }
    
    /**
     * Test the request path file format argument with a welcome file and an
     * empty string value for the path.
     */
    @Test
    public void formatEmptyPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector("", "index.html"));
        assertEquals(formatter.format("/new/%s", PathFile.class), "/new/index.html");
    }
}
