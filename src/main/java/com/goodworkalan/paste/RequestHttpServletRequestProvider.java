package com.goodworkalan.paste;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the root HTTP servlet request.
 * 
 * @author Alan Gutierrez
 */
public class RequestHttpServletRequestProvider implements Provider<HttpServletRequest> {
    /**
     * The filtration created for the first invocation of the Paste filter for
     * this request.
     */
    private final Filtration filtration;

    /**
     * Create a request provider that will return the first request encountered
     * by the paste filter.
     * 
     * @param filtration
     *            The first filtration created by the request filter.
     */
    @Inject
    public RequestHttpServletRequestProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }
    
    /**
     * Get the root HTTP servlet request.
     * 
     * @return The root HTTP servlet request.
     */
    public HttpServletRequest get() {
        return filtration.getRequest();
    }
}
