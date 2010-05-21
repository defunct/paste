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
    private final String where;

    /**
     * Create an exception to throw to trigger an HTTP redirection.
     * 
     * @param where
     *            The redirection URL.
     */
    public Redirection(String where) {
        this.where = where;
    }

    /**
     * Get the redirection URL.
     * 
     * @return The redirection URL.
     */
    public String getWhere() {
        return where;
    }
}
