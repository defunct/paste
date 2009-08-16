package com.goodworkalan.paste;

import com.goodworkalan.paste.janitor.JanitorQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provides a queue of janitors whose <code>cleanUp</code> method will be
 * invoked with the filter and all of the filters and servlets further down the
 * filter chain complete.
 * 
 * @author Alan Gutierrez
 */
public class FilterJanitorQueueProvider implements Provider<JanitorQueue> {
    /** The filtration created for the current invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a provider that provides a queue of janitors whose
     * <code>cleanUp</code> method will be invoked with the filter and all of
     * the filters and servlets further down the filter chain complete.
     * 
     * @param filtration
     *            The filtration created for the current invocation of the paste
     *            filter.
     */
    @Inject
    public FilterJanitorQueueProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get a queue of janitors whose <code>cleanUp</code> method will be invoked
     * with the filter and all of the filters and servlets further down the
     * filter chain complete.
     * 
     * @return A queue of janitors whose <code>cleanUp</code> method will be
     *         invoked with the filter and all of the filters and servlets
     *         further down the filter chain complete.
     */
    public JanitorQueue get() {
        return new JanitorQueue(filtration.getFilterJanitors());
    }
}
