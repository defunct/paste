package com.goodworkalan.paste.stencil;

import java.net.URI;

/**
 * Configuration for a Stencil renderer.
 *
 * @author Alan Gutierrez
 */
class Configuration {
    /** The format to use to create the redirection URL. */
    public String format;

    /** The format arguments to use to create the redirection URL. */
    public Class<?>[] formatArguments = new Class<?>[0];
    
    /** The base URI from which to locate Stencils. */
    public URI baseURI;
    
    /** The mime type to send. */
    public String contentType;
}
