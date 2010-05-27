package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

import com.goodworkalan.paste.controller.Headers;

public class ReponseHeadersProvider implements Provider<Headers> {
    private final InterceptingResponse response;

    public ReponseHeadersProvider(InterceptingResponse response) {
        this.response = response;
    }

    public Headers get() {
        return new Headers(response.getHeaders());
    }
}
