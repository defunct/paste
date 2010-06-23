package com.goodworkalan.paste.servlet;

import java.util.regex.Matcher;

import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Test the suffix regular expression. 
 *
 * @author Alan Gutierrez
 */
public class SuffixTest {
    /**
     * Return the suffix found in the given path or null if no suffix is found.
     * 
     * @param path
     *            The file path.
     * @return The file suffix.
     */
    public String suffix(String path) {
        Matcher matcher = Responder.SUFFIX.matcher(path);
        if (matcher.matches()) {
            return matcher.group(2);
        }
        return null;
    }
    
    /** Get the suffix. */
    @Test
    public void suffixes() {
        assertEquals(suffix("/hello.txt"), "txt");
        assertNull(suffix("/hello"));
        assertNull(suffix("/static.directory/hello"));
        assertEquals(suffix("/static.directory/hello.txt"), "txt");
        assertEquals(suffix("/static.directory/hello.tar.gz"), "tar.gz");
    }
}
