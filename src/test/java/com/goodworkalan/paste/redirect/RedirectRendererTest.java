package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.paste.NamedValue;
import com.goodworkalan.paste.ResponseHeaders;
import com.goodworkalan.paste.paths.FormatArgument;
import com.goodworkalan.paste.paths.PathFormatter;
import com.goodworkalan.paste.redirect.Configuration;
import com.goodworkalan.paste.redirect.RedirectRenderer;
import com.goodworkalan.paste.redirect.Redirector;
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
        
        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
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
        
        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
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
