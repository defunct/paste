package com.goodworkalan.paste.stream;

/**
 * Configuration for a stream response.
 * 
 * @author Alan Gutierrez
 */
class Configuration {
    /** The method name to call or null if we match on mime type. */
    public String methodName;

    /** The mime type to send. */
    public String contentType;
}
