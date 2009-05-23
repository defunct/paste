package com.goodworkalan.paste.redirect;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.ResponseHeaders;
import com.goodworkalan.paste.redirect.Redirector;

public class RedirectorTest
{
    @Test
    public void constructor()  
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        new Redirector(request, headers);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void badStatus()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("/foo", 200);
    }

    @Test
    public void absolute()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getContextPath()).thenReturn("/foo");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("/bar");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/bar");
    }
    
    @Test
    public void relative()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("bar");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/bar");
    }

    @Test
    public void relativeIndex()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/");
    }    

    @Test
    public void remote()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(headers.getFirst("Location"), "http://netscape.com/");
    }
    
    @Test
    public void parameters()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        
        Redirector redirector = new Redirector(request, headers);

        redirector.parameter(null, null);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(headers.getFirst("Location"), "http://netscape.com/");
    }
}
