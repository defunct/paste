package com.goodworkalan.paste;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the response specific to the current invocation of the paste filter.
 * 
 * @author Alan Gutierrez
 */
public class HttpServletResponseProvider implements Provider<HttpServletResponse> {
    /** The filtration created for the current invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a provider to provide the response specific to the current
     * invocation of the paste filter.
     * 
     * @param filtration
     *            The filtration created for the current invocation of the paste
     *            filter.
     */
    @Inject
    public HttpServletResponseProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }
    
    /**
     * Return the response specific to the current invocation of the paste
     * filter.
     * 
     * @return The response specific to the current invocation of the paste
     *         filter.
     */
    public HttpServletResponse get() {
        return filtration.getResponse();
    }
}
