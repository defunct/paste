package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Paste exception thrown by all failed paste operations.
 * 
 * @author Alan Gutierrez
 */
public class PasteException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 20080620L;

    /** The error code. */
    private final int code;

    /** The paste filter was called out of the context of a servlet engine. */
    public final static int CALLED_OUT_OF_CONTEXT = 101;

    /** A list of arguments to the formatted error message. */
    private final List<Object> arguments = new ArrayList<Object>();

    /**
     * Create a Sheaf exception with the given error code.
     * 
     * @param code
     *            The error code.
     */
    public PasteException(int code) {
        super();
        this.code = code;
    }

    public PasteException(Throwable cause) {
        super(cause);
        this.code = 0;
    }
    
    public PasteException() {
        this(0);
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
    public PasteException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**
     * Get the error code.
     * 
     * @return The error code.
     */
    public int getCode() {
        return code;
    }

    /**
     * Add an argument to the list of arguments to provide the formatted error
     * message associated with the error code.
     * 
     * @param argument
     *            The format argument.
     * @return This sheaf exception for chained invocation of add.
     */
    public PasteException add(Object... arguments) {
        for (Object argument : arguments) {
            this.arguments.add(argument);
        }
        return this;
    }

    /**
     * Create an detail message from the error message format associated with
     * the error code and the format arguments.
     * 
     * @return The exception message.
     */
    @Override
    public String getMessage() {
        String key = Integer.toString(code);
        ResourceBundle exceptions = ResourceBundle.getBundle("com.goodworkalan.addendum.exceptions");
        String format;
        try {
            format = exceptions.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
        try {
            return String.format(format, arguments.toArray());
        } catch (Throwable e) {
            throw new Error(key, e);
        }
    }
}
