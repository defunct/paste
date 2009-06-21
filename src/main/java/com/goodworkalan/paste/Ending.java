package com.goodworkalan.paste;

public class Ending<T>
{
    private final T parent;
    
    public Ending(T parent)
    {
        this.parent = parent;
    }
    
    public T end()
    {
        return parent;
    }
}
