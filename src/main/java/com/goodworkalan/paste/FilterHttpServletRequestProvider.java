package com.goodworkalan.paste;

import javax.servlet.http.HttpServletRequest;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the HTTP request specific to the current invocation of the paste
 * filter.
 * 
 * @author Alan Gutierrez
 */
public class FilterHttpServletRequestProvider implements Provider<HttpServletRequest> {
    /** The filtration created for the current invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a request provider that will return the request specific to the
     * current invocation of the paste filter.
     * 
     * @param filtration
     *            The filtration created for the current invocation of the paste
     *            filter.
     */
    @Inject
    public FilterHttpServletRequestProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get the HTTP request specific to the current invocation of the paste
     * filter.
     * 
     * @return The HTTP request specific to the current invocation of the paste
     *         filter.
     */
    public HttpServletRequest get() {
        return filtration.getRequest();
    }
}