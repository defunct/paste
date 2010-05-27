package com.goodworkalan.paste.actor;

/**
 * A wrapper around an exception raised by an object that supports an actor that
 * was constructed through dependency injection. This is to disambiguate
 * exceptions thrown through reflection that are controller exceptions
 * (application exceptions) or actor exceptions (framework exceptions).
 * Framework exceptions are reported as programming errors, while controller
 * exceptions can alter the flow of control of the application.
 * 
 * This is a wrapper only exception,
 * 
 * @author Alan Gutierrez
 */
public class ActorException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    public ActorException(Throwable cause) {
        super("An exception was thrown by a system actor.", cause);
    }
}
