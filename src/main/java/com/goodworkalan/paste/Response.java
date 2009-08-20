package com.goodworkalan.paste;

import com.goodworkalan.paste.util.NamedValueList;

/**
 * An alternate interface for the HTTP response that records the response status
 * and response headers for inspection during the rendering switch.
 * 
 * @author Alan Gutierrez
 */
public interface Response {
    /** The format for HTTP dates, ISO 8601. */
    public final static String HTTP_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    /**
     * Set the response status without flipping the intercept flag. This method
     * is overridden to record the status code for the {@link Response}
     * interface.
     * 
     * @param status
     *            The status code.
     */
    public void setStatus(int status);

    /**
     * Get the status code set.
     * 
     * @return The status code.
     */
    public int getStatus();

    /**
     * Return the list of headers that were set through this response.
     * 
     * @return The list of response headers.
     */
    public NamedValueList getHeaders();

    /**
     * Sets a response header with the given name and date-value. The date is
     * specified in terms of milliseconds since the epoch. If the header had
     * already been set, the new value overwrites the previous one.
     * 
     * 
     * @param name
     *            The header name.
     * @param date
     *            The date value.
     */
    public void setDateHeader(String name, long date);

    /**
     * Adds a response header with the given name and date-value. The date is
     * specified in terms of milliseconds since the epoch.
     * 
     * @param name
     *            The header name.
     * @param date
     *            The date value.
     */
    public void addDateHeader(String name, long date);

    /**
     * Sets a response header with the given name and integer value. If the
     * header had already been set, the new value overwrites the previous one.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The integer value.
     */
    public void setIntHeader(String name, int value);

    /**
     * Adds a response header with the given name and integer value.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The integer value.
     */
    public void addIntHeader(String name, int value);

    /**
     * Sets a response header with the given name and value. If the header had
     * already been set, the new value overwrites the previous one.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The value.
     */
    public void setHeader(String name, String value);

    /**
     * Adds a response header with the given name and value.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The value.
     */
    public void addHeader(String name, String value);
}