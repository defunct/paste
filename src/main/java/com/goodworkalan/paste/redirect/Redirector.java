package com.goodworkalan.paste.redirect;
import static com.goodworkalan.paste.redirect.Redirects.isRedirectStatus;

import java.net.URI;

import com.goodworkalan.paste.Request;
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
    private final Request request;
    
    // TODO Document.
    private final Response response;
    
    // TODO Document.
    @Inject
    public Redirector(Request request, Response response)
    {
        this.request = request;
        this.response = response;
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

        response.setStatus(status);
        
        URI location = URI.create(where.trim());
        if (location.getScheme() == null)
        {
            URI uri = URI.create(request.getRequestURL().toString());
            String path = location.getPath();
            if (path.length() != 0 && path.charAt(0) == '/')
            {
                path = request.getContextPath() + path;
                response.addHeader("Location", uri.resolve(path).toString());
            }
            else
            {
                response.addHeader("Location", uri.resolve(path).toString());
            }
        }
        else
        {
            response.addHeader("Location", location.toString());
        }
    }

    public void parameter(String name, String value)
    {
    }
}