package com.goodworkalan.paste.redirect;

import com.goodworkalan.paste.paths.FormatArgument;

/**
 * Configuration a redirection response.
 *
 * @author Alan Gutierrez
 */
class Configuration
{
    /** The response status. */
    private final int status;
    
    /** The format to use to create the redirection URL. */
    private final String format;
    
    /** The format arguments to use to create the redirection URL. */
    private final FormatArgument[] formatArguments;

    /**
     * Create a configuration for a redirection response.
     * 
     * @param status
     *            The response status.
     * @param format
     *            The format to use to create the redirection URL.
     * @param formatArguments
     *            The format arguments to use to create the redirection URL.
     */
    public Configuration(int status, String format, FormatArgument[] formatArguments)
    {
        this.status = status;
        this.format = format;
        this.formatArguments = formatArguments;
    }
    
    /**
     * Get the response status.
     * 
     * @return The response status.
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * Get the format to use to create the redirection URL.
     * 
     * @return The format to use to create the redirection URL.
     */
    public String getFormat()
    {
        return format;
    }

    /**
     * Get the format arguments to use to create the redirection URL.
     * 
     * @return The format arguments to use to create the redirection URL.
     */
    public FormatArgument[] getFormatArguments()
    {
        return formatArguments;
    }
}
