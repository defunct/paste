package com.goodworkalan.guicelet.invoke;


public class Widget
{
    public int i = 0;
    
    @Invoke(on = "save", arguments = { "number" })
    public void foo(int i)
    {
    }
}
