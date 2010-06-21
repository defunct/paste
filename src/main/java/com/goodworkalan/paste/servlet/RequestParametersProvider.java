package com.goodworkalan.paste.servlet;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Request;

// TODO Document.
public class RequestParametersProvider implements Provider<Parameters> {
    // TODO Document.
    private final Parameters parameters;
    
    // TODO Document.
    @Inject
    public RequestParametersProvider(@Request Parameters parameters) {
        this.parameters = parameters;
    }
    
    // TODO Document.
    public Parameters get() {
        return parameters;
    }
}
