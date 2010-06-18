package com.goodworkalan.paste.controller;

import com.goodworkalan.danger.CodedDanger;

/**
 * Paste exception thrown by all failed paste operations.
 * 
 * @author Alan Gutierrez
 */
public class PasteException extends CodedDanger {
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;
    
    /** The paste filter was called out of the context of a servlet engine. */
    public final static int CALLED_OUT_OF_CONTEXT = 201;

    public final static int CONTROLLER_CONSTRUCTION_EXCEPTION = 201;

    public final static int ACTOR_EXCEPTION = 202;
    
    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public PasteException(int code, Object...arguments) {
        super(code, null, arguments);
    }
    
    /**
     * Wrap the given cause exception in an addendum exception with the given
     * error code.
     * 
     * @param code
     *            The error code.
     * @param cause
     *            The cause exception.
     */
    public PasteException(int code, Throwable cause, Object...arguments) {
        super(code, cause, arguments);
    }
}
