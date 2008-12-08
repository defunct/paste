package com.goodworkalan.guicelet.redirect;
import static com.goodworkalan.guicelet.redirect.Redirects.isRedirectStatus;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.guicelet.Headers;


public class Redirector
{
    private final HttpServletRequest request;
    
    private final Headers headers;
    
    public Redirector(HttpServletRequest request, Headers headers)
    {
        this.request = request;
        this.headers = headers;
    }
    
    public void redirect(String where)
    {
        redirect(where, 303);
    }
    
    public void redirect(String where, int status)
    {
        if (!isRedirectStatus(status))
        {
            throw new IllegalArgumentException();
        }

        headers.setStatus(status);
        
        URI uri = URI.create(request.getRequestURI());
        URI location = URI.create(where.trim());
        if (location.getScheme() == null)
        {
            String path = location.getPath();
            if (path.length() != 0 && path.charAt(0) == '/')
            {
                path = request.getServletPath() + path;
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
