package com.goodworkalan.paste.intercept;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.goodworkalan.paste.BasicScope;

// TODO Document.
public class InterceptingRequest extends HttpServletRequestWrapper
{
    // TODO Document.
    private final Interception interception;
    
    private final BasicScope controllerScope;
    
    // TODO Document.
    public InterceptingRequest(Interception interception, HttpServletRequest request, BasicScope controllerScope)
    {
        super(request);
        this.interception = interception;
        this.controllerScope = controllerScope;
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
                controllerScope.push();
                try
                {
                    delegate.forward(request, response);
                }
                finally
                {
                    controllerScope.pop();
                }
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
                controllerScope.push();
                try
                {
                    delegate.include(request, response);
                }
                finally
                {
                    controllerScope.pop();
                }
            }
        };
    }
}
