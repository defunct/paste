package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.paste.MockHttpServletRequest;
import com.goodworkalan.paste.Request;
import com.goodworkalan.paste.Response;
import com.goodworkalan.paste.paths.FormatArgument;
import com.goodworkalan.paste.paths.PathFormatter;
import com.google.inject.Guice;

public class RedirectRendererTest
{
    @Test
    public void boundRedirect() throws ServletException, IOException
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/bar"));

        StringWriter writer = new StringWriter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        
        PathFormatter formatter = new PathFormatter(Guice.createInjector());
        
        Response r = new Response(response);
        
        Redirector redirector = new Redirector(new Request(request.getRequest()), r);
        
        Configuration configuration = new Configuration(303, "home", new FormatArgument[0]);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                r,
                new StringWriter(),
                redirector,
                configuration);

        renderer.render();
    }

    public void badLocation() throws ServletException, IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/bar"));

        StringWriter writer = new StringWriter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        
        PathFormatter formatter = new PathFormatter(Guice.createInjector());
        
        Response r = new Response(response);
        Redirector redirector = new Redirector(new Request(request), r);
        
        Configuration configuration = new Configuration(303, "home", new FormatArgument[0]);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                r,
                new StringWriter(),
                redirector,
                configuration);

        renderer.render();
    }
}
