package com.goodworkalan.guicelet.redirect;

import static org.testng.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Headers;

public class RedirectorTest
{
    @Test
    public void constructor()  
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Headers headers = new Headers("GET");
        new Redirector(request, headers);
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void badStatus()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("/foo", 200);
    }

    @Test
    public void absolute()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getServletPath()).thenReturn("/foo");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("/bar");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/bar");
    }
    
    @Test
    public void relative()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("bar");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/bar");
    }

    @Test
    public void relativeIndex()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("");
        
        assertEquals(headers.getFirst("Location"), "http://domain.com/foo/");
    }    

    @Test
    public void remote()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(headers.getFirst("Location"), "http://netscape.com/");
    }
    
    @Test
    public void parameters()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);

        Headers headers = new Headers("GET");
        
        Redirector redirector = new Redirector(request, headers);

        redirector.parameter(null, null);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(headers.getFirst("Location"), "http://netscape.com/");
    }
}
