package com.goodworkalan.guicelet.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.GuiceletException;
import com.goodworkalan.guicelet.Headers;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.Request;
import com.goodworkalan.guicelet.paths.PathFormatter;
import com.google.inject.Inject;

// TODO Document.
public class RedirectRenderer implements Renderer
{
    // TODO Document.
    private final PathFormatter pathFormatter;
    
    // TODO Document.
    private final HttpServletResponse response;
    
    // TODO Document.
    private final Object controller;
    
    // TODO Document.
    private final Redirector redirector;

    // TODO Document.
    private final Headers headers;

    // TODO Document.
    private final Configuration configuration;
    
    // TODO Document.
    @Inject
    public RedirectRenderer(
            PathFormatter pathFormatter,
            HttpServletResponse response,
            @Controller Object controller,
            Redirector redirector,
            @Request Headers headers,
            Configuration configuration)
    {
        this.pathFormatter = pathFormatter;
        this.response = response;
        this.controller = controller;
        this.redirector = redirector;
        this.headers = headers;
        this.configuration = configuration;
    }

    // TODO Document.
    public void render() throws ServletException, IOException
    {
        if (headers.get("Location") == null)
        {
            SuggestedRedirection suggestedRedirection = controller.getClass().getAnnotation(SuggestedRedirection.class);
            if (suggestedRedirection != null)
            {
                redirector.redirect(suggestedRedirection.value());
            }
        }

        if (configuration.getFormat() != null)
        {
            String path = pathFormatter.format(configuration.getFormat(), configuration.getFormatArguments());
            redirector.redirect(path, configuration.getStatus());
        }
        
        if (!Redirects.isRedirectStatus(headers.getStatus()))
        {
            throw new GuiceletException();
        }
        else if (headers.get("Location") == null)
        {
            throw new GuiceletException();
        }
        else
        {
            try
            {
                new URI(headers.getFirst("Location"));
            }
            catch (URISyntaxException e)
            {
                throw new GuiceletException(e);
            }
        }
        
        headers.send(response);
 
        String page = 
            String.format(getPageFormat(),
                          headers.getStatus(), headers.get("Location"));
        response.getWriter().append(page);
    }
    
    // TODO Document.
    private String getPageFormat() throws IOException
    {
        Reader reader = new InputStreamReader(RedirectRenderer.class.getResourceAsStream("redirect.html"));
        StringBuilder newString = new StringBuilder();
        char[] buffer = new char[2048];
        int read;
        while ((read = reader.read(buffer)) != -1)
        {
            newString.append(buffer, 0, read);
        }
        return newString.toString();
    }
}
