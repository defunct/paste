package com.goodworkalan.guicelet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

// TODO Document.
public class InterceptingHttpResponse extends HttpServletResponseWrapper
{
    // TODO Document.
    private final Interception interception;
    
    // TODO Document.
    private PrintWriter writer;
    
    // TODO Document.
    private ServletOutputStream out;
    
    // TODO Document.
    public InterceptingHttpResponse(Interception interception, HttpServletResponse response)
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

    @Override
    public PrintWriter getWriter() throws IOException
    {
        if (writer == null)
        {
            writer = new PrintWriter(new InterceptingWriter(interception, super.getWriter()));
        }
        return writer;
    }

    // TODO Document.
    @Override
    public void sendError(int sc) throws IOException
    {
        interception.intercept();
        super.sendError(sc);
    }
    
    // TODO Document.
    @Override
    public void sendError(int sc, String msg) throws IOException
    {
        interception.intercept();
        super.sendError(sc, msg);
    }
    
    // TODO Document.
    @Override
    public void sendRedirect(String location) throws IOException
    {
        interception.intercept();
        super.sendRedirect(location);
    }
    
    // TODO Document.
    @Override
    public void flushBuffer() throws IOException
    {
        interception.intercept();
        super.flushBuffer();
    }
}
