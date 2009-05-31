package com.goodworkalan.paste;

// FIXME Rename.
public class Abnormality extends Stop
{
    private static final long serialVersionUID = 1L;
    
    private final int status;
    
    public Abnormality(int statusCode)
    {
        this.status = statusCode;
    }
    
    public Abnormality(int status, Throwable cause)
    {
        super(cause);
        this.status = status;
    }
    
    public int getStatus()
    {
        return status;
    }
}
