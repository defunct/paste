package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

import com.goodworkalan.paste.controller.Headers;

/**
 * Returns a copy of the current state of the response headers. The copy is
 * read only and will not reflect any changes made to the response headers after
 * the copy is created.
 * 
 * @author Alan Gutierrez
 */
public class ReponseHeadersProvider implements Provider<Headers> {
    /** The intercepting HTTP response. */
    private final InterceptingResponse response;

    /**
     * Create a response headers provider.
     * @param response The intercepting HTTP response.
     */
    public ReponseHeadersProvider(InterceptingResponse response) {
        this.response = response;
    }

    /**
     * Return a copy of the current state of the response headers.
     * 
     * @return A copy of the current state of the response headers.
     */
    public Headers get() {
        return new Headers(response.getHeaders());
    }
}
