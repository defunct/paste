package com.goodworkalan.paste.intercept;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

// TODO Document.
public class InterceptingResponse extends HttpServletResponseWrapper
{
    // TODO Document.
    private final Interception interception;

    // TODO Document.
    public InterceptingResponse(Interception interception, HttpServletResponse response)
    {
        super(response);
        this.interception = interception;
    }
    
    // TODO Document.
    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        interception.intercept();
        return getResponse().getOutputStream();
    }

    // TODO Document.
    @Override
    public PrintWriter getWriter() throws IOException
    {
        interception.intercept();
        return getResponse().getWriter();
    }
    
    // TODO Document.
    @Override
    public void flushBuffer() throws IOException
    {
        interception.intercept();
        super.flushBuffer();
    }
}
