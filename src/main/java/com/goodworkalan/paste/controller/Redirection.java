package com.goodworkalan.paste.controller;


/**
 * An exception thrown by controllers to trigger an HTTP redirection.
 * 
 * @author Alan Gutierrez
 */
public class Redirection extends Stop {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** The redirection URL. */
    public final String format;
    
    /** The status code. */
    public final int status;
    
    // TODO Document.
    public final Class<?>[] formatArguments;

    /**
     * Create an exception to throw to trigger an HTTP redirection.
     * 
     * @param where
     *            The redirection URL.
     */
    public Redirection(String where, Class<?>... formatArguments) {
        this(where, 303);
    }
    
    // TODO Document.
    public Redirection(Class<?>... formatArguments) {
        this(null, 303, formatArguments);
    }

    // TODO Document.
    public Redirection(String where, int status, Class<?>... formatArguments) {
        this.format = where;
        this.status = status;
        this.formatArguments = formatArguments;
    }
}
