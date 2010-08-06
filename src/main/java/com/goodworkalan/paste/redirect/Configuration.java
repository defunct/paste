package com.goodworkalan.paste.redirect;

/**
 * Configuration for a redirection response.
 * 
 * @author Alan Gutierrez
 */
class Configuration {
    /** The status code to set during the redirection. */
    public int status = 303;

    /** The format to use to create the redirection URL. */
    public String format;

    /** The format arguments to use to create the redirection URL. */
    public Class<?>[] formatArguments = new Class<?>[0];
}
