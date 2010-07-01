package com.goodworkalan.paste.controller;

import java.lang.annotation.Annotation;

import com.goodworkalan.ilk.Ilk;

/**
 * Triggers a reaction, which is an event that is not directly associated with
 * an HTTP request. This is used by timers to implement delayed jobs or
 * background tasks. Reaction controllers are built using dependency injection,
 * so that application scoped resources are available during the reaction.
 * 
 * @author Alan Gutierrez
 */
public interface Reactor {
    /**
     * Trigger a reaction of the type specified by the given type token with the
     * given object instance. Reactions are bound to types so that information
     * can be provided via an instance of the type.
     * 
     * @param <T>
     *            The event type.
     * @param lik
     *            The super type token of the event type.
     * @param object
     *            The event.
     */
    public <T> void react(Ilk<T> lik, T object);
    
    
    /**
     * Trigger a reaction of the type specified by the given type token.
     * Reactions are bound to types so that information can be provided via an
     * instance of the type.
     * 
     * @param <T>
     *            The event type.
     * @param ilk
     *            The super type token of the event type.
     */
    public <T> void react(Ilk<T> ilk);
    
    /**
     * Trigger a reaction that generates an event that qualifies an object with
     * the given annotation.
     * 
     * @param annotation
     *            The annotation.
     */
    public void react(Class<? extends Annotation> annotation);
}
