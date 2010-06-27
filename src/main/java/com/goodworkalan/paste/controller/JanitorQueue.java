package com.goodworkalan.paste.controller;

import java.util.Collection;

/**
 * A queue of <code>Runnable</code> instances that are run to release resources
 * allocated by the application. Different queues are created for different stages
 * of event processing.
 * 
 * @author Alan Gutierrez
 */
public class JanitorQueue {
    /** The collection of janitors. */
    private final Collection<Runnable> janitors;

    /**
     * Create a janitor queue around the given collection of janitors.
     * @param janitors The collection of janitors.
     */
    public JanitorQueue(Collection<Runnable> janitors) {
        this.janitors = janitors;
    }

    /**
     * Add a janitor to the janitor queue.
     * 
     * @param janitor
     *            The janitor.
     */
    public void add(Runnable janitor) {
        janitors.add(janitor);
    }
}
