package com.goodworkalan.guicelet;

public class Interception
{
    private boolean intercepted;
    
    public void intercept()
    {
        intercepted = true;
    }
    
    public boolean isIntercepted()
    {
        return intercepted;
    }
}
