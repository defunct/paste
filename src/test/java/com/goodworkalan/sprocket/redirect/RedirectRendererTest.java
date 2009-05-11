package com.goodworkalan.sprocket.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.sprocket.Headers;
import com.goodworkalan.sprocket.paths.FormatArgument;
import com.goodworkalan.sprocket.paths.PathFormatter;
import com.goodworkalan.sprocket.redirect.Configuration;
import com.goodworkalan.sprocket.redirect.RedirectRenderer;
import com.goodworkalan.sprocket.redirect.Redirector;
import com.google.inject.Guice;

public class RedirectRendererTest
{
    @Test
    public void boundRedirect() throws ServletException, IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/bar"));

        StringWriter writer = new StringWriter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        
        PathFormatter formatter = new PathFormatter(Guice.createInjector());
        
        Headers headers = new Headers("GET");
        Redirector redirector = new Redirector(request, headers);
        
        Configuration configuration = new Configuration(303, "home", new FormatArgument[0]);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                response,
                redirector,
                headers,
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
        
        Headers headers = new Headers("GET");
        Redirector redirector = new Redirector(request, headers);
        
        Configuration configuration = new Configuration(303, null, null);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                response,
                redirector,
                headers,
                configuration);

        renderer.render();
    }
}
