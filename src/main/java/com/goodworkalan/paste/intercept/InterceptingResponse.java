package com.goodworkalan.paste.intercept;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.goodworkalan.paste.Response;
import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.NamedValueList;

/**
 * A wrapper around an HTTP Servlet response that detects if a response has been
 * sent. This is possibly more sensitive that the the <code>isCommitted</code>.
 * It marks a response as sent the moment that an error is sent, a redirect is
 * sent or any content is written to the response output stream or writer.
 * 
 * @author Alan Gutierrez
 * 
 */
public class InterceptingResponse extends HttpServletResponseWrapper implements Response {
    /** A thread local date formatter. */
    private final static ThreadLocal<DateFormat> format = new ThreadLocal<DateFormat>();
    
    /** The list of headers. */
    private final List<NamedValue> headers;

    /** The intercept flag. */
    private final Interception interception;

    /** The response status code. */
    private int status;
    
    /**
     * Create an intercepting output stream that wraps the given response and
     * flips the given incerception flag if any meaningful response is sent.
     * 
     * @param interception
     *            The intercept flag.
     * @param response
     *            The wrapped response.
     */
    public InterceptingResponse(Interception interception, HttpServletResponse response) {
        super(response);
        this.headers = new ArrayList<NamedValue>();
        this.interception = interception;
    }

    /**
     * Sets the length of the content body in the response, setting the
     * <code>Content-Length</code> header.
     * 
     * @param len
     *            The length of the content being returned to the client.
     */
    @Override
    public void setContentLength(int len) {
        headers.add(new NamedValue(NamedValue.RESPONSE, "Content-Length", Integer.toString(len)));
        super.setContentLength(len);
    }

    /**
     * Sets the content type of the response being sent to the client, if the
     * response has not been committed yet.
     * 
     * @param type
     *            The content type.
     */
    @Override
    public void setContentType(String type) {
        headers.add(new NamedValue(NamedValue.RESPONSE, "Content-Type", type));
        super.setContentType(type);
    }

    /**
     * Get the thread local date formatter or create it if it does not exist.
     * 
     * @return The thread local date formatter.
     */
    private static DateFormat getDateFormat() {
        DateFormat dateFormat = format.get();
        if (dateFormat == null) {
            format.set(new SimpleDateFormat(Response.HTTP_DATE_FORMAT));
            return getDateFormat();
        }
        return dateFormat;
    }

    /**
     * Get an output stream suitable for writing binary data and flip the
     * intercept flag.
     * 
     * @return An output stream suitable for writing binary data to the
     *         response.
     */
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        interception.intercept();
        return super.getOutputStream();
    }

    /**
     * Get a writer that can write character data and flip the intercept flag.
     * 
     * @return A writer that can write character data to the response.
     */
    @Override
    public PrintWriter getWriter() throws IOException {
        interception.intercept();
        return super.getWriter();
    }

    /**
     * Send an error response and flip the intercept flag.
     * 
     * @param sc
     *            The error status code.
     */
    @Override
    public void sendError(int sc) throws IOException {
        interception.intercept();
        super.sendError(sc);
    }

    /**
     * Send an error response and flip the intercept flag.
     * 
     * @param sc
     *            The error status code.
     * @param msg
     *            The error message.
     */
    @Override
    public void sendError(int sc, String msg) throws IOException {
        interception.intercept();
        super.sendError(sc, msg);
    }

    /**
     * Set the response status without flipping the intercept flag. This method
     * is overridden to record the status code for the {@link Response}
     * interface.
     * 
     * @param sc
     *            The status code.
     */
    @Override
    public void setStatus(int sc) {
        super.setStatus(status = sc);
    }
    
    /**
     * Get the status code set.
     * 
     * @return The status code.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Send a redirection and flip the intercept flag.
     * 
     * @param location
     *            The location URL.
     */
    @Override
    public void sendRedirect(String location) throws IOException {
        interception.intercept();
        super.sendRedirect(location);
    }

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
    @Override
    public void setDateHeader(String name, long date) {
        clearHeader(name);
        headers.add(new NamedValue(NamedValue.RESPONSE, name, getDateFormat().format(new Date(date))));
        super.setDateHeader(name, date);
    }

    /**
     * Adds a response header with the given name and date-value. The date is
     * specified in terms of milliseconds since the epoch.
     * 
     * @param name
     *            The header name.
     * @param date
     *            The date value.
     */
    @Override
    public void addDateHeader(String name, long date) {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, getDateFormat().format(new Date(date))));
        super.addDateHeader(name, date);
    }

    /**
     * Return the list of headers that were set through this response.
     * 
     * @return The list of response headers.
     */
    public NamedValueList getHeaders() {
        return new NamedValueList(headers);
    }

    /**
     * Sets a response header with the given name and integer value. If the
     * header had already been set, the new value overwrites the previous one.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The integer value.
     */
    @Override
    public void setIntHeader(String name, int value) {
        clearHeader(name);
        headers.add(new NamedValue(NamedValue.RESPONSE, name, Integer.toString(value)));
        super.setIntHeader(name, value);
    }

    /**
     * Adds a response header with the given name and integer value.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The integer value.
     */
    @Override
    public void addIntHeader(String name, int value) {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, Integer.toString(value)));
        super.addIntHeader(name, value);
    }

    /**
     * Sets a response header with the given name and value. If the header had
     * already been set, the new value overwrites the previous one.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The value.
     */
    @Override
    public void setHeader(String name, String value) {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, value));
        super.setHeader(name, value);
    }

    /**
     * Adds a response header with the given name and value.
     * 
     * @param name
     *            The header name.
     * @param value
     *            The value.
     */
    @Override
    public void addHeader(String name, String value) {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, value));
        super.addHeader(name, value);
    }

    /**
     * Remove the the headers with the given name.
     * 
     * @param name
     *            The header name.
     */
    private void clearHeader(String name) {
        Iterator<NamedValue> namedValues = headers.iterator();
        while (namedValues.hasNext()) {
            NamedValue namedValue = namedValues.next();
            if (namedValue.getName().equals(name)) {
                namedValues.remove();
            }
        }
    }
    
    /**
     * Force any content in the buffer to be written to the client and flip the
     * intercept flag.
     */
    @Override
    public void flushBuffer() throws IOException {
        interception.intercept();
        super.flushBuffer();
    }
}
