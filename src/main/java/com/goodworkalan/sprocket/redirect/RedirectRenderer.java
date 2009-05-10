package com.goodworkalan.sprocket.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.sprocket.Headers;
import com.goodworkalan.sprocket.Renderer;
import com.goodworkalan.sprocket.Response;
import com.goodworkalan.sprocket.SprocketException;
import com.goodworkalan.sprocket.paths.PathFormatter;
import com.google.inject.Inject;

// TODO Document.
public class RedirectRenderer implements Renderer
{
    // TODO Document.
    private final PathFormatter pathFormatter;
    
    // TODO Document.
    private final HttpServletResponse response;
    
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
            Redirector redirector,
            @Response Headers headers,
            Configuration configuration)
    {
        this.pathFormatter = pathFormatter;
        this.response = response;
        this.redirector = redirector;
        this.headers = headers;
        this.configuration = configuration;
    }

    /**
     * Write a redirection header and body to the HTTP response.
     */
    public void render() throws ServletException, IOException
    {
        if (configuration.getFormat() != null)
        {
            String path = pathFormatter.format(configuration.getFormat(), configuration.getFormatArguments());
            redirector.redirect(path, configuration.getStatus());
        }
        
        if (!Redirects.isRedirectStatus(headers.getStatus()))
        {
            throw new SprocketException();
        }
        else if (headers.get("Location") == null)
        {
            throw new SprocketException();
        }
        else
        {
            try
            {
                new URI(headers.getFirst("Location"));
            }
            catch (URISyntaxException e)
            {
                throw new SprocketException(e);
            }
        }
        
        headers.send(response);
 
        String page = 
            String.format(getPageFormat(),
                          headers.getStatus(), headers.get("Location"));
        response.getWriter().append(page);
    }

    /**
     * Read the page format for the redirection page from a resource file.
     * 
     * @return The page format for the redirection page.
     * @throws IOException
     *             If an I/O error occurs.
     */
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
