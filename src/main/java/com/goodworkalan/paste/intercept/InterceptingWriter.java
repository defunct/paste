package com.goodworkalan.paste.intercept;

import java.io.IOException;
import java.io.Writer;

// TODO Document.
public class InterceptingWriter extends Writer
{
    // TODO Document.
    private final Interception interception;
    
    // TODO Document.
    private final Writer delegate;
    
    // TODO Document.
    public InterceptingWriter(Interception interception, Writer delegate)
    {
        this.interception = interception;
        this.delegate = delegate;
    }

    // TODO Document.
    @Override
    public void write(int c) throws IOException
    {
        delegate.write(c);
    }

    // TODO Document.
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException
    {
        interception.intercept();
        delegate.write(cbuf, off, len);
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
