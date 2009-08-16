package com.goodworkalan.paste.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;

import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.Responder;
import com.goodworkalan.paste.Response;
import com.goodworkalan.paste.paths.PathFormatter;
import com.google.inject.Inject;

// TODO Document.
public class RedirectRenderer implements Renderer
{
    // TODO Document.
    private final PathFormatter pathFormatter;
    
    // TODO Document.
    private final Redirector redirector;

    // TODO Document.
    private final Response response;

    // TODO Document.
    private final Configuration configuration;
    
    // TODO Document.
    private final Responder responder;
    
    // TODO Document.
    @Inject
    public RedirectRenderer(PathFormatter pathFormatter, Response response, Responder responder, Redirector redirector, Configuration configuration)
    {
        this.pathFormatter = pathFormatter;
        this.response = response;
        this.responder = responder;
        this.redirector = redirector;
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
        
        if (!Redirects.isRedirectStatus(response.getStatus()))
        {
            throw new PasteException(0);
        }
        else if (response.getHeaders().getFirst("Location") == null)
        {
            throw new PasteException(0);
        }
        else
        {
            try
            {
                new URI(response.getHeaders().getFirst("Location"));
            }
            catch (URISyntaxException e)
            {
                throw new PasteException(0, e);
            }
        }
        
        responder.send(response);
 
        String page = 
            String.format(getPageFormat(),
                          response.getStatus(), response.getHeaders().getFirst("Location"));
        responder.getWriter().append(page);
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
