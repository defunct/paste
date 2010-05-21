package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.controller.scopes.RequestScoped;

/**
 * A formatter that formats a sprintf string using objects built from the
 * Guice injector.
 * 
 * @author Alan Gutierrez.
 */
@RequestScoped
public class PathFormatter {
    /** The Guice injector. */
    private final Injector injector;

    /**
     * Construct a path formatter with the given injector.
     * 
     * @param injector The injector.
     */
    @Inject
    public PathFormatter(Injector injector) {
        this.injector = injector;
    }

    /**
     * Format the sprintf format string using format objects created from the
     * dependency injector for the current filter.
     * 
     * @param format
     *            The sprintf format.
     * @param formatArguments
     *            The type of objects to crate.
     * @return A formatted string.
     */
    public String format(String format, Class<?>[] formatArguments) {
        Object[] arguments = new Object[formatArguments.length];
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = injector.instance(formatArguments[i], null);
        }
        return String.format(format, arguments);
    }
}
