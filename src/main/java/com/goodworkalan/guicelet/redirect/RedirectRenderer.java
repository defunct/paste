package com.goodworkalan.guicelet.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Headers;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.Response;

public class RedirectRenderer implements Renderer
{
    private final static String pageFormat;
    
    private final Object controller;
    
    private final HttpServletResponse response;
    
    private final Redirector redirector;
    
    private final Headers headers;
    
    static
    {
        Reader reader = new InputStreamReader(RedirectRenderer.class.getResourceAsStream("redirect.html"));
        StringBuilder newString = new StringBuilder();
        char[] buffer = new char[2048];
        int read;
        try
        {
            while ((read = reader.read(buffer)) != -1)
            {
                newString.append(buffer, 0, read);
            }
        }
        catch (IOException e)
        {
            throw new Error(e);
        }
        pageFormat = newString.toString();
    }
    
    public RedirectRenderer(
            @Controller Object controller,
            HttpServletResponse response,
            Redirector redirector,
            @Response Headers headers)
    {
        this.controller = controller;
        this.response = response;
        this.redirector = redirector;
        this.headers = headers;
    }

    public void render() throws ServletException, IOException
    {
        if (!headers.contains("Location"))
        {
            SuggestedRedirection suggestedRedirection = controller.getClass().getAnnotation(SuggestedRedirection.class);
            if (suggestedRedirection != null)
            {
                redirector.redirect(suggestedRedirection.value());
            }
        }
        if (headers.getStatus() == 0)
        {
            headers.setStatus(303);
        }
        headers.send(response);
 
        String page = 
            String.format(pageFormat,
                          headers.getStatus(), headers.get("Location"));
        response.getWriter().append(page);
    }
}
