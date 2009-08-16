package com.goodworkalan.paste;

import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class RequestJanitorQueueProvider implements Provider<JanitorQueue> {
    /**
     * The filtration created for the first invocation of the Paste filter for
     * this request.
     */
    private final Filtration filtration;
    
    @Inject
    public RequestJanitorQueueProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }
    
    public JanitorQueue get() {
        return new JanitorQueue(filtration.getRequestJanitors());
    }
}
