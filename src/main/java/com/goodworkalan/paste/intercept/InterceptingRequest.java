package com.goodworkalan.paste.intercept;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

// TODO Document.
public class InterceptingRequest extends HttpServletRequestWrapper
{
    // TODO Document.
    private final Interception interception;
    
    // TODO Document.
    public InterceptingRequest(Interception interception, HttpServletRequest request)
    {
        super(request);
        this.interception = interception;
    }
    
    // TODO Document.
//    @Override
//    public ServletResponse getServletResponse()
//    {
//        return new InterceptingResponse(interception, (HttpServletResponse) super.getServletResponse());
//    }
    
    // TODO Document.
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
                while (request instanceof InterceptingRequest)
                {
                    request = ((InterceptingRequest) request).getRequest();
                }
                while (response instanceof InterceptingResponse)
                {
                    response = ((InterceptingResponse) response).getResponse();
                }
                delegate.forward(request, response);
            }
            
            public void include(ServletRequest request, ServletResponse response)
                    throws ServletException, IOException
            {
                interception.intercept();
                while (request instanceof InterceptingRequest)
                {
                    request = ((InterceptingRequest) request).getRequest();
                }
                while (response instanceof InterceptingResponse)
                {
                    response = ((InterceptingResponse) response).getResponse();
                }
                delegate.include(request, response);
            }
        };
    }
}
