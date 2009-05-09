package com.goodworkalan.sprocket;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;

// TODO Document.
public class InterceptingOutputStream extends ServletOutputStream
{
    // TODO Document.
    private final Interception interception;
    
    // TODO Document.
    private final OutputStream delegate;
    
    // TODO Document.
    public InterceptingOutputStream(Interception interception, OutputStream delegate)
    {
        this.interception = interception;
        this.delegate = delegate;
    }
    
    // TODO Document.
    @Override
    public void write(int b) throws IOException
    {
        interception.intercept();
        delegate.write(b);
    }
    
    // TODO Document.
    @Override
    public void write(byte[] b) throws IOException
    {
        interception.intercept();
        delegate.write(b);
    }
    
    // TODO Document.
    @Override
    public void write(byte[] b, int off, int len) throws IOException
    {
        interception.intercept();
        delegate.write(b, off, len);
    }
    
    // TODO Document.
    @Override
    public void flush() throws IOException
    {
        interception.intercept();
        delegate.flush();
    }

    // TODO Document.
    @Override
    public void close() throws IOException
    {
        interception.intercept();
        delegate.close();
    }
}
