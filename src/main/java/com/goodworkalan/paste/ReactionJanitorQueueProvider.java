package com.goodworkalan.paste;

import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;

/**
 * Provides a queue of janitors that will cleanup when the first filter
 * completes.
 * 
 * @author Alan Gutierrez
 */
public class ReactionJanitorQueueProvider {
    /** The filtration created for the first invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a provider that provides a queue of janitors that will cleanup
     * when the first filter completes.
     * 
     * @param filtration
     *            The filtration created for the first invocation of the paste
     *            filter.
     */
    @Inject
    public ReactionJanitorQueueProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get a queue of janitors that will cleanup when the all of the filters and
     * servlets further down the filter chain complete.
     * 
     * @return A queue of janitors that will cleanup when the first call to the
     *         filter completes.
     */
    public JanitorQueue get() {
        return new JanitorQueue(filtration.getJanitors());
    }
}
