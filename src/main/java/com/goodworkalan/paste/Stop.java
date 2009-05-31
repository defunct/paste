package com.goodworkalan.paste;

public class Stop extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public Stop()
    {
    }
    
    public Stop(Throwable cause)
    {
        super(cause);
    }
}
