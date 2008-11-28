package com.goodworkalan.guicelet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

public class InterceptingOutputStream extends ServletOutputStream
{
    private final Interception interception;
    
    private final OutputStream delegate;
    
    public InterceptingOutputStream(Interception interception, OutputStream delegate)
    {
        this.interception = interception;
        this.delegate = delegate;
    }
    
    @Override
    public void write(int b) throws IOException
    {
        interception.intercept();
        delegate.write(b);
    }
    
    @Override
    public void write(byte[] b) throws IOException
    {
        interception.intercept();
        delegate.write(b);
    }
    
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        interception.intercept();
        delegate.write(b, off, len);
    }
    
    @Override
    public void flush() throws IOException
    {
        interception.intercept();
        delegate.flush();
    }

    @Override
    public void close() throws IOException
    {
        interception.intercept();
        delegate.close();
    }
}
