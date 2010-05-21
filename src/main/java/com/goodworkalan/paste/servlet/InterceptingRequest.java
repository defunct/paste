package com.goodworkalan.paste.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * A wrapper around an HTTP Servlet response that detects if a response has been
 * sent. This wrapper will detect a response sent using the
 * <code>RequestDispatcher</code> returned by <code>getRequestDispatcher</code>.
 * 
 * @author Alan Gutierrez
 */
public class InterceptingRequest extends HttpServletRequestWrapper {
    /** The intercept flag. */
    private final Interception interception;

    /**
     * Create a wrapper around an HTTP Servlet response that detects if a
     * response has been sent.
     * 
     * @param interception
     *            The intercept flag.
     * @param request
     *            The request to wrap.
     */
    public InterceptingRequest(Interception interception,
            HttpServletRequest request) {
        super(request);
        this.interception = interception;
    }

    /**
     * Return a <code>RequestDispatcher</code> that will flip the intercept flag
     * if the request is forwarded or if other content is included.
     * 
     * @return A request dispatcher that will detect a response.
     */
    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        final RequestDispatcher delegate = super.getRequestDispatcher(path);

        return new RequestDispatcher() {
            public void forward(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
                interception.intercept();
                while (request instanceof InterceptingRequest) {
                    request = ((InterceptingRequest) request).getRequest();
                }
                while (response instanceof InterceptingResponse) {
                    response = ((InterceptingResponse) response).getResponse();
                }
                delegate.forward(request, response);
            }

            public void include(ServletRequest request, ServletResponse response)
            throws ServletException, IOException {
                interception.intercept();
                while (request instanceof InterceptingRequest) {
                    request = ((InterceptingRequest) request).getRequest();
                }
                while (response instanceof InterceptingResponse) {
                    response = ((InterceptingResponse) response).getResponse();
                }
                delegate.include(request, response);
            }
        };
    }
}
