package com.goodworkalan.paste;

import com.google.inject.Inject;
import com.google.inject.Provider;

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
