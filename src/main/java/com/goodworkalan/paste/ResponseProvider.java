package com.goodworkalan.paste;

import javax.inject.Inject;
import javax.inject.Provider;

public class ResponseProvider implements Provider<Response> {
    private final Filtration filtration;
    
    @Inject
    public ResponseProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }

    public Response get() {
        return filtration.getResponse();
    }
}
