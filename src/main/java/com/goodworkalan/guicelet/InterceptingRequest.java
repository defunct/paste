package com.goodworkalan.guicelet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class InterceptingRequest extends HttpServletRequestWrapper
{
    private final Interception interception;
    
    public InterceptingRequest(Interception interception, HttpServletRequest request)
    {
        super(request);
        this.interception = interception;
    }
    
    @Override
    public ServletResponse getServletResponse()
    {
        return new InterceptingResponse(interception, super.getServletResponse());
    }
    
    @Override
    public RequestDispatcher getRequestDispatcher(String path)
    {
        final RequestDispatcher delegate = super.getRequestDispatcher(path);
        return new RequestDispatcher()
        {
            public void forward(ServletRequest request, ServletResponse response)
                    throws ServletException, IOException
            {
                interception.intercept();
                delegate.forward(request, response);
            }
            
            public void include(ServletRequest request, ServletResponse response)
                    throws ServletException, IOException
            {
                interception.intercept();
                delegate.include(request, response);
            }
        };
    }
}
