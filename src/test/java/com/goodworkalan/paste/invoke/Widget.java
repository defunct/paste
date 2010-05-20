package com.goodworkalan.paste.invoke;

import com.goodworkalan.paste.invoke.Invoke;

public class Widget {
    public int i = 0;

    @Invoke(on = "save", arguments = { "number" })
    public void foo(int i) {
    }
}
