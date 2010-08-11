package com.goodworkalan.paste.actor;

import com.goodworkalan.ilk.inject.InjectException;
import com.goodworkalan.ilk.inject.Injector;

/**
 * Wrapper for an exception thrown by a controller.
 * <p>
 * This must be used carefully to only wrap excpetions thrown by controllers,
 * but using the depdency injector {@link Injector} and catching only
 * {@link InjectException} is a good way to ensure that the invocation target of
 * an <code>InvocationTargetException</code> was a controller or other
 * application provided exception. Paste system objects built through injection
 * will wrap any thrown exceptions in a <code>ActorException</code>, and
 * those will be ignored. If you implement an actor you should wrap any exceptions 
 * you might throw in an <code>ActorException</code>.
 * 
 * @author Alan Gutierrez
 */
public class ControllerException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;
    
    /** The controller injector. */
    public final Injector injector;

    /**
     * Create a controller exception that wraps the given cause.
     * 
     * @param cause
     *            The cause.
     * @param controllerClass
     *            The controller class.
     */
    public ControllerException(Throwable cause, Injector injector) {
        super("Controller threw an exception.", cause);
        this.injector = injector;
    }
}
