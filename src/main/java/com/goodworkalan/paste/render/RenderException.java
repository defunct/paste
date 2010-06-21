package com.goodworkalan.paste.render;

import java.util.Arrays;
import java.util.IllegalFormatException;

// TODO Document.
public class RenderException extends RuntimeException {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    // TODO Document.
    public RenderException(String message, Throwable cause) {
        super(message, cause);
    }

    // TODO Document.
    public static String _(String message, Object... arguments) {
        try {
            return String.format(message, arguments);
        } catch (IllegalFormatException e) {
            return String.format("Format error message [%s] for format [%s] using arguments %s.", e.getMessage(), message, Arrays.asList(arguments));
        }
    }
}
