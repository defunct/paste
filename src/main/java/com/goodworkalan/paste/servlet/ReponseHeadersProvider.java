package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

import com.goodworkalan.paste.controller.Headers;

// TODO Document.
public class ReponseHeadersProvider implements Provider<Headers> {
    // TODO Document.
    private final InterceptingResponse response;

    // TODO Document.
    public ReponseHeadersProvider(InterceptingResponse response) {
        this.response = response;
    }

    // TODO Document.
    public Headers get() {
        return new Headers(response.getHeaders());
    }
}
