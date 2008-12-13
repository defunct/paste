package com.goodworkalan.guicelet.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Headers;
import com.goodworkalan.guicelet.Transfer;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.goodworkalan.guicelet.paths.PathFormatter;

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
        
        Transfer transfer = mock(Transfer.class);
        
        PathFormatter formatter = new PathFormatter(transfer);
        
        Headers headers = new Headers("GET");
        Redirector redirector = new Redirector(request, headers);
        
        Configuration configuration = new Configuration(303, "home", new FormatArgument[0]);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                response,
                new Object(),
                redirector,
                headers,
                configuration);

        renderer.render();
    }

    @Test
    public void badLocation() throws ServletException, IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/bar"));

        StringWriter writer = new StringWriter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        
        Transfer transfer = mock(Transfer.class);
        
        PathFormatter formatter = new PathFormatter(transfer);
        
        Headers headers = new Headers("GET");
        Redirector redirector = new Redirector(request, headers);
        
        Configuration configuration = new Configuration(303, null, null);
        
        RedirectRenderer renderer = new RedirectRenderer(
                formatter,
                response,
                new Pseudo(),
                redirector,
                headers,
                configuration);

        renderer.render();
    }
}
