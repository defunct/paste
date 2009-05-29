package com.goodworkalan.paste;

import java.io.IOException;
import java.io.OutputStreamWriter;
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
    private PrintWriter writer;
    
    // TODO Document.
    private ServletOutputStream out;
    
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
        interception.intercept();
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
