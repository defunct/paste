package com.goodworkalan.paste.forward;

import com.goodworkalan.paste.RequestScoped;
import com.goodworkalan.paste.paths.FormatArgument;

/**
 * A configuration structure for the forward renderer.
 * 
 * @author Alan Gutierrez
 */
@RequestScoped
class Configuration {
    /** The request property name to use to store the controller. */
    private final String property;

    /** The format to use to create the forward path. */
    private final String format;

    /** The format arguments to use to create the forward path. */
    private final FormatArgument[] formatArguments;

    /**
     * Create a forward configuration with the given controller property, the
     * given format and the given format arguments.
     * 
     * @param property
     *            The request property name to use to store the controller.
     * @param format
     *            The format to use to create the forward path.
     * @param formatArguments
     *            The format arguments to use to create the forward path.
     */
    public Configuration(String property, String format, FormatArgument[] formatArguments) {
        this.property = property;
        this.format = format;
        this.formatArguments = formatArguments;
    }

    /**
     * Get the request property name to use to store the controller.
     * 
     * @return The request property name to use to store the controller.
     */
    public String getProperty() {
        return property;
    }

    /**
     * Get the format to use to create the forward path.
     * 
     * @return The format to use to create the forward path.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Get the format arguments to use to create the forward path.
     * 
     * @return The format arguments to use to create the forward path.
     */
    public FormatArgument[] getFormatArguments() {
        return formatArguments;
    }
}