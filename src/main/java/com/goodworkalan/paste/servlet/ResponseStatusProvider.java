package com.goodworkalan.paste.servlet;

import javax.inject.Provider;

public class ResponseStatusProvider implements Provider<Integer> {
    private final InterceptingResponse response;
    
    public ResponseStatusProvider(InterceptingResponse response) {
        this.response = response;
    }
    
    public Integer get() {
        return response.getStatus();
    }
}
