package com.goodworkalan.paste;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Provide the HTTP session for the duration of a request.
 * 
 * @author Alan Gutierrez
 */
public class HttpSessionProvider implements Provider<HttpSession> {
    /** The request from the first invocation of the filter. */
    private final HttpServletRequest request;

    /**
     * Create an HTTP session provider that will provide the HTTP session of the
     * given request.
     * 
     * @param request
     *            The request from the first invocation of the filter.
     */
    @Inject
    public HttpSessionProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get the HTTP session.
     * 
     * @return The HTTP session.
     */
    public HttpSession get() {
        return request.getSession();
    }
}
