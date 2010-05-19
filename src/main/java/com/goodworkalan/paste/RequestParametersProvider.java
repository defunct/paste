package com.goodworkalan.paste;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.paste.util.Parameters;

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
