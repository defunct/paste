package com.goodworkalan.sprocket.invoke;

import com.goodworkalan.sprocket.invoke.Invoke;


public class Widget
{
    public int i = 0;
    
    @Invoke(on = "save", arguments = { "number" })
    public void foo(int i)
    {
    }
}
