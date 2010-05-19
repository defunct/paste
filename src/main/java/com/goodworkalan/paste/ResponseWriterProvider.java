package com.goodworkalan.paste;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.inject.Provider;

public class ResponseWriterProvider implements Provider<PrintWriter> {
    public final Filtration filtration;
    
    @Inject
    public ResponseWriterProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }
    
    public PrintWriter get() {
        try {
            return filtration.getResponse().getWriter();
        } catch (IOException e) {
            throw new PasteException(0, e);
        }
    }
}
