package com.goodworkalan.paste.redirect;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.goodworkalan.paste.redirect.Redirects;

// TODO Document.
public class RedirectsTest {
    // TODO Document.
    @Test
    public void isRedirectStatus() {
        new Redirects();
        assertFalse(Redirects.isRedirectStatus(200));
        assertTrue(Redirects.isRedirectStatus(300));
        assertTrue(Redirects.isRedirectStatus(301));
        assertTrue(Redirects.isRedirectStatus(302));
        assertTrue(Redirects.isRedirectStatus(303));
        assertFalse(Redirects.isRedirectStatus(304));
        assertFalse(Redirects.isRedirectStatus(305));
        assertFalse(Redirects.isRedirectStatus(306));
        assertTrue(Redirects.isRedirectStatus(307));
        assertFalse(Redirects.isRedirectStatus(400));
    }
}
