package com.goodworkalan.paste.controller;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Utility class for launching reactions delayed or at intervals.
 *
 * @author Alan Gutierrez
 */
public class Reactions {
    /** The timer. */
    private final static Timer TIMER = new Timer("Paste Reactions", true);

    /** The reactor. */
    private final Reactor reactor;

    /**
     * Create a new reaction utility.
     * 
     * @param reactor
     *            The reactor.
     */
    public Reactions(Reactor reactor) {
        this.reactor = reactor;
    }

    /**
     * Trigger the reaction associated with the given annotation at the given
     * interval in seconds starting at the top of the next minute.
     * 
     * @param annotation
     *            The annotation.
     * @param interval
     *            The interval in seconds.
     */
    public void periodic(final Class<? extends Annotation> annotation, int interval) {
        periodic(new ReactionTask<Object>(reactor, null, null, annotation), interval * 1000);
    }

    /**
     * Trigger the reaction associated with the given annotation after the given
     * delay in seconds.
     * 
     * @param annotation
     *            The annotation.
     * @param interval
     *            The interval in seconds.
     */
    public void delayed(final Class<? extends Annotation> annotation, int delay) {
        TIMER.schedule(new ReactionTask<Object>(reactor, null, null, annotation), delay * 1000);
    }

    /**
     * Run the given timer task at the given interval in seconds starting at the
     * top of the next minute.
     * 
     * @param annotation
     *            The annotation.
     * @param interval
     *            The interval in seconds.
     */
    private static void periodic(TimerTask runnable, int interval) {
        Date first = new Date();
        first.setTime((first.getTime() - first.getTime() % 60000) + 65000);
        TIMER.scheduleAtFixedRate(runnable, first, interval * 1000);
    }
}
