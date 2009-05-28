package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.paste.MockHttpServletRequest;
import com.goodworkalan.paste.Mocks;
import com.goodworkalan.paste.Request;
import com.goodworkalan.paste.Response;

public class RedirectorTest
{
    @Test
    public void constructor()  
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        new Redirector(new Request(request.getRequest()), new Response());
    }

    @Test(expectedExceptions=IllegalArgumentException.class)
    public void badStatus()
    {
        HttpServletRequest request = Mocks.request();
        
        Redirector redirector = new Redirector(new Request(request), new Response());
        redirector.redirect("/foo", 200);
    }

    @Test
    public void absolute()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getContextPath()).thenReturn("/foo");
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));
        
        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        redirector.redirect("/bar");
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://domain.com/foo/bar");
    }
    
    @Test
    public void relative()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        redirector.redirect("bar");
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://domain.com/foo/bar");
    }

    @Test
    public void relativeIndex()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));

        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        redirector.redirect("");
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://domain.com/foo/");
    }    

    @Test
    public void remote()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://netscape.com/");
    }
    
    @Test
    public void parameters()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();

        Response r = new Response();
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);

        redirector.parameter(null, null);
        redirector.redirect("http://netscape.com/");
        
        assertEquals(r.getHeaders().getFirst("Location"), "http://netscape.com/");
    }
}
