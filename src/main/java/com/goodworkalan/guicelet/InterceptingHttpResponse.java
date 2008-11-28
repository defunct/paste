package com.goodworkalan.guicelet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class InterceptingHttpResponse extends HttpServletResponseWrapper
{
    private final Interception interception;
    
    private PrintWriter writer;
    
    private ServletOutputStream out;
    
    public InterceptingHttpResponse(Interception interception, HttpServletResponse response)
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
    public void sendError(int sc) throws IOException
    {
        interception.intercept();
        super.sendError(sc);
    }
    
    @Override
    public void sendError(int sc, String msg) throws IOException
    {
        interception.intercept();
        super.sendError(sc, msg);
    }
    
    @Override
    public void sendRedirect(String location) throws IOException
    {
        interception.intercept();
        super.sendRedirect(location);
    }
    
    @Override
    public void flushBuffer() throws IOException
    {
        interception.intercept();
        super.flushBuffer();
    }
}
