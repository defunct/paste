package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

/**
 * Returns the current response status.
 * 
 * @author Alan Gutierrez
 */
public class ResponseStatusProvider implements Provider<Integer> {
    /** The intercepting HTTP response. */
    private final InterceptingResponse response;
    
    /**
     * Create response status provider.
     * 
     * @param response
     *            The intercepting HTTP response.
     */
    public ResponseStatusProvider(InterceptingResponse response) {
        this.response = response;
    }

    /**
     * Get the current response status.
     * 
     * @return The current response status.
     */
    public Integer get() {
        return response.getStatus();
    }
}
