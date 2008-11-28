package com.goodworkalan.guicelet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;

public class InterceptingResponse extends ServletResponseWrapper
{
    private final Interception interception;

    private PrintWriter writer;
    
    private ServletOutputStream out;
    
    public InterceptingResponse(Interception interception, ServletResponse response)
    {
        super(response);
        this.interception = interception;
    }
    
    @Override
    public ServletOutputStream getOutputStream() throws IOException
    {
        if (out == null)
        {
            out = new InterceptingOutputStream(interception, super.getOutputStream());
        }
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException
    {
        if (writer == null)
        {
            writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
        }
        return writer;
    }
    
    @Override
    public void flushBuffer() throws IOException
    {
        interception.intercept();
        super.flushBuffer();
    }
}
