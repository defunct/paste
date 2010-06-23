package com.goodworkalan.paste.controller;

/**
 * An exception thrown by controllers to indicate an error in the HTTP
 * transaction.
 * 
 * @author Alan Gutierrez
 */
public class Stop extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /** The HTTP response status. */
    private final int status;

    /**
     * Create a new abnormality with the given HTTP status code.
     * 
     * @param statusCode
     *            The HTTP response status.
     */
    public Stop(int statusCode) {
        this.status = statusCode;
    }

    /**
     * Create a new abnormality with the given HTTP status code caused by the
     * given exception.
     * 
     * @param statusCode
     *            The HTTP response status.
     * @param cause
     *            The cause of the abnormality.
     */
    public Stop(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }

    /**
     * Get the HTTP response status.
     * 
     * @return The HTTP response status.
     */
    public int getStatus() {
        return status;
    }
}
