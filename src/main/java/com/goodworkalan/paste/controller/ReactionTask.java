package com.goodworkalan.paste.controller;

import java.lang.annotation.Annotation;
import java.util.TimerTask;

import com.goodworkalan.ilk.Ilk;

/**
 * Trigger a reaction from within a timer. This class implements all flavors of
 * reaction invocation, intentionally combining them, instead of creating an
 * implementation for each <code>react</code> method in <code>Reactor</code>.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The reagent type.
 */
class ReactionTask<T> extends TimerTask {
    /** The reactor. */
    private final Reactor reactor;
    
    /** The super type token of the reagent. */
    private final Ilk<T> ilk;
    
    /** The reagent. */
    private final T object;
    
    /** The annotation to bind to an <code>Object</code> reagent. */
    private final Class<? extends Annotation> annotation;

    /**
     * Create a new reaction task.
     * 
     * @param reactor
     *            The reactor.
     * @param ilk
     *            The super type token of the reagent.
     * @param reagent
     *            The reagent.
     * @param annotation
     *            The annotation to bind to an <code>Object</code> reagent.
     */
    public ReactionTask(Reactor reactor, Ilk<T> ilk, T object, Class<? extends Annotation> annotation) {
        this.reactor = reactor;
        this.ilk = ilk;
        this.object = object;
        this.annotation = annotation;
    }

    /**  Trigger a reaction. */
    @Override
    public void run() {
        if (object != null) {
            reactor.react(ilk, object);
        } else if (ilk != null) {
            reactor.react(ilk);
        } else {
            reactor.react(annotation);
        }        
    }
}
