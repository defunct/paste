package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

// TODO Document.
public class ResponseStatusProvider implements Provider<Integer> {
    // TODO Document.
    private final InterceptingResponse response;
    
    // TODO Document.
    public ResponseStatusProvider(InterceptingResponse response) {
        this.response = response;
    }
    
    // TODO Document.
    public Integer get() {
        return response.getStatus();
    }
}
