package com.goodworkalan.sprocket;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

// TODO Document.
public class InterceptingResponse extends ServletResponseWrapper
{
    // TODO Document.
    private final Interception interception;

    // TODO Document.
    private PrintWriter writer;
    
    // TODO Document.
    private ServletOutputStream out;
    
    // TODO Document.
    public InterceptingResponse(Interception interception, ServletResponse response)
    {
        super(response);
        this.interception = interception;
    }
    
    // TODO Document.
    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        if (out == null)
        {
            out = new InterceptingOutputStream(interception, super.getOutputStream());
        }
        return out;
    }

    // TODO Document.
    @Override
    public PrintWriter getWriter() throws IOException
    {
        if (writer == null)
        {
            writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
        }
        return writer;
    }
    
    // TODO Document.
    @Override
    public void flushBuffer() throws IOException
    {
        interception.intercept();
        super.flushBuffer();
    }
}
