package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.redirect.Redirector;
import com.google.inject.Inject;

// TODO Just use Injector.
public class Transfer
{
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;

    private final Object controller;

    private final String path;
    
    private final String welcomeFile;
    
    private final Redirector redirector;
    
    private final Headers requestHeaders;
    
    @Inject
    public Transfer(
            HttpServletRequest request,
            HttpServletResponse response,
            @Controller Object controller,
            @Path String path,
            @WelcomeFile String welcomeFile,
            Redirector redirector,
            @Request Headers requestHeaders
            )
    {
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.path = path;
        this.welcomeFile = welcomeFile;
        this.redirector = redirector;
        this.requestHeaders = requestHeaders;
    }
    
    public HttpServletRequest getHttpServletRequest()
    {
        return request;
    }
    
    public HttpServletResponse getHttpServletResponse()
    {
        return response;
    }
    
    public Object getController()
    {
        return controller;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public String getWelcomeFile()
    {
        return welcomeFile;
    }

    public Redirector getRedirector()
    {
        return redirector;
    }
    
    public Headers getRequestHeaders()
    {
        return requestHeaders;
    }
}
