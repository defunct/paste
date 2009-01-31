package com.goodworkalan.guicelet;

import java.io.IOException;
import java.io.Writer;

public class InterceptingWriter extends Writer
{
    private final Interception interception;
    
    private final Writer delegate;
    
    public InterceptingWriter(Interception interception, Writer delegate)
    {
        this.interception = interception;
        this.delegate = delegate;
    }

    @Override
    public void write(int c) throws IOException
    {
        delegate.write(c);
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        interception.intercept();
        delegate.write(cbuf, off, len);
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
