package com.goodworkalan.guicelet.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Headers;

public class RedirectionTest
{
    @Test
    public void constructor()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        
        Redirection redirection = new Redirection("bar").parameter("hello", "world");
        redirection.redirect(redirector);
        
        assertEquals(headers.get("Location"), "http://domain.com/foo/bar");   
    }
}
