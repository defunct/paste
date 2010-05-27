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
    
    public Redirection(Class<?>... formatArguments) {
        this(null, 303, formatArguments);
    }

    public Redirection(String where, int status, Class<?>... formatArguments) {
        this.format = where;
        this.status = status;
        this.formatArguments = formatArguments;
    }
}
