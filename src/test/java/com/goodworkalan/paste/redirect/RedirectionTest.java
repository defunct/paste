package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.ResponseHeaders;
import com.goodworkalan.paste.redirect.Redirection;
import com.goodworkalan.paste.redirect.Redirector;

public class RedirectionTest
{
    @Test
    public void constructor()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>(), "GET");
        
        Redirector redirector = new Redirector(request, headers);
        
        Redirection redirection = new Redirection("bar");
        redirection.redirect(redirector);
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/bar");   
    }
}
