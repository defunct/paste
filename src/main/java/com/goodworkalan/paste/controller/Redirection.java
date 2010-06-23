package com.goodworkalan.paste.controller;


/**
 * An exception thrown by controllers to trigger an HTTP redirection.
 * 
 * @author Alan Gutierrez
 */
public class Redirection extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** The redirection URL format. */
    public final String whereFormat;
    
    /** The status code. */
    public final int status;
    
    /** The redirection URL format arguments. */
    public final Class<?>[] whereFormatArguments;

    /**
     * Create an exception to throw to trigger an HTTP redirection that
     * redirects to the URL generated from the given string format using the
     * given format arguments.
     * 
     * @param whereFormat
     *            The redirection URL format.
     * @param whereFormatArguments
     *            The format arguments for the URL form.
     */
    public Redirection(String whereFormat, Class<?>... whereFormatArguments) {
        this(303, whereFormat, whereFormatArguments);
    }

    /**
     * Create an exception to throw to trigger an HTTP redirection with the
     * given redirection code that redirects to the URL generated from the given
     * string format using the given format arguments.
     * 
     * @param status
     *            The HTTP result status.
     * @param whereFormat
     *            The redirection URL format.
     * @param whereFormatArguments
     *            The format arguments for the URL form.
     */
    public Redirection(int status, String whereFormat, Class<?>... whereFormatArguments) {
        this.status = status;
        this.whereFormat = whereFormat;
        this.whereFormatArguments = whereFormatArguments;
    }
}
