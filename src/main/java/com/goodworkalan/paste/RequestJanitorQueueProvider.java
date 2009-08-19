package com.goodworkalan.paste;

import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the janitor queue for janitors that will clean up when the first
 * invocation of the Paste filter is complete.
 * 
 * @author Alan Gutierrez
 */
public class RequestJanitorQueueProvider implements Provider<JanitorQueue> {
    /**
     * The filtration created for the first invocation of the Paste filter for
     * this request.
     */
    private final Filtration filtration;

    /**
     * Create a provider to provide the janitor queue for janitors that will clean
     * up when the first invocation of the Paste filter is complete.
     * 
     * @param filtration
     *            The filtration created for the first invocation of the Paste
     *            filter for this request.
     */
    @Inject
    public RequestJanitorQueueProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get a janitor queue for janitors that will clean up when the first
     * invocation of the Paste filter is complete.
     * 
     * @return The janitor queue.
     */
    public JanitorQueue get() {
        return new JanitorQueue(filtration.getJanitors());
    }
}
