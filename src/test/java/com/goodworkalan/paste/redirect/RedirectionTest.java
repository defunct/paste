package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.paste.MockHttpServletRequest;
import com.goodworkalan.paste.Request;
import com.goodworkalan.paste.Response;

public class RedirectionTest
{
    @Test
    public void constructor()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        
        Redirection redirection = new Redirection("bar");
        redirector.redirect(redirection.getWhere());
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://domain.com/foo/bar");   
    }
}
