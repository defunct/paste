package com.goodworkalan.paste.stream;

/**
 * Configuration for a stream response.
 * 
 * @author Alan Gutierrez
 */
class Configuration
{
    /** The method name to call or null if we match on mime type. */
    private final String methodName;
    
    /** The mime type to send. */
    private final String contentType;

    /**
     * Create a new stream configuration.
     * 
     * @param methodName
     *            The method name to call.
     * @param mimeType
     *            The mime type.
     */
    public Configuration(String methodName, String mimeType)
    {
        this.methodName = methodName;
        this.contentType = mimeType;
    }
    
    /**
     * Get the method name to call or null if the method is to determined
     * by the mime-type in the {@link Output} annotation.
     *  
     * @return The method name to call or null.
     */
    public String getMethodName()
    {
        return methodName;
    }
    
    /**
     * Get the mime-type to send.
     *  
     * @return The mime-type to send.
     */
    public String getContentType()
    {
        return contentType;
    }
}
