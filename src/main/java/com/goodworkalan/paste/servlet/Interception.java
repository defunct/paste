package com.goodworkalan.paste.servlet;

/**
 * A container for an intercept flag. An intercept flag is used to detect when a
 * response is sent. When a response is sent, the rest of the chain is not
 * called. This container is given to an intercepting response and request,
 * either of which can flip the intercept flag to indicate that a response has
 * been sent.
 * 
 * @author Alan Gutierrez
 */
public class Interception {
    /** The intercept flag. */
    private boolean intercepted;

    /** Set the intercept flag to true. */
    public void intercept() {
        intercepted = true;
    }

    /**
     * Return whether or not a response has been sent.
     * 
     * @return True if a response has been sent.
     */
    public boolean isIntercepted() {
        return intercepted;
    }
}
