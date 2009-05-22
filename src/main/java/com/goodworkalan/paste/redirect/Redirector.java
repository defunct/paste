package com.goodworkalan.paste.redirect;
import static com.goodworkalan.paste.redirect.Redirects.isRedirectStatus;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.ResponseHeaders;
import com.goodworkalan.paste.Response;
import com.google.inject.Inject;

/**
 * Initiate a redircecion.
 * 
 * @author Alan Gutierrez
 */
// TODO Document.
public class Redirector
{
    // TODO Document.
    private final HttpServletRequest request;
    
    // TODO Document.
    private final ResponseHeaders headers;
    
    // TODO Document.
    @Inject
    public Redirector(HttpServletRequest request, @Response ResponseHeaders headers)
    {
        this.request = request;
        this.headers = headers;
    }
    
    // TODO Document.
    public void redirect(String where)
    {
        redirect(where, 303);
    }
    
    // TODO Document.
    public void redirect(String where, int status)
    {
        if (!isRedirectStatus(status))
        {
            throw new IllegalArgumentException();
        }

        headers.setStatus(status);
        
        URI location = URI.create(where.trim());
        if (location.getScheme() == null)
        {
            URI uri = URI.create(request.getRequestURL().toString());
            String path = location.getPath();
            if (path.length() != 0 && path.charAt(0) == '/')
            {
                path = request.getContextPath() + path;
                headers.add("Location", uri.resolve(path).toString());
            }
            else
            {
                headers.add("Location", uri.resolve(path).toString());
            }
        }
        else
        {
            headers.add("Location", location.toString());
        }
    }

    public void parameter(String name, String value)
    {
    }
}
