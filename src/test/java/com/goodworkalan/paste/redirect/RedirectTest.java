package com.goodworkalan.paste.redirect;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * Unit tests for the {@link Redirect} class.
 *
 * @author Alan Gutierrez
 */
public class RedirectTest {
    /** Test redirect status detection. */
    @Test
    public void isRedirectStatus() {
        assertFalse(Redirect.isRedirectStatus(200));
        assertTrue(Redirect.isRedirectStatus(300));
        assertTrue(Redirect.isRedirectStatus(301));
        assertTrue(Redirect.isRedirectStatus(302));
        assertTrue(Redirect.isRedirectStatus(303));
        assertFalse(Redirect.isRedirectStatus(304));
        assertFalse(Redirect.isRedirectStatus(305));
        assertFalse(Redirect.isRedirectStatus(306));
        assertTrue(Redirect.isRedirectStatus(307));
        assertFalse(Redirect.isRedirectStatus(400));
    }
}
