package com.goodworkalan.paste.paths;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link PathFull} class.
 *
 * @author Alan Gutierrez
 */
public class PathFullTest extends FormatTest {
    /** Test the full path format argument. */
    @Test
    public void formatFullPath() {
        PathFormatter formatter = new PathFormatter(getPathInjector("/path/file.html"));
        assertEquals(formatter.format("%s", PathFull.class), "/path/file.html");
    }
}
