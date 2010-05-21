package com.goodworkalan.paste.providers;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Request;

public class RequestParametersProvider implements Provider<Parameters> {
    private final Parameters parameters;
    
    @Inject
    public RequestParametersProvider(@Request Parameters parameters) {
        this.parameters = parameters;
    }
    
    public Parameters get() {
        return parameters;
    }
}
