package com.goodworkalan.paste.invoke;

import com.goodworkalan.paste.invoke.Invoke;

// TODO Document.
public class Widget {
    // TODO Document.
    public int i = 0;

    // TODO Document.
    @Invoke(on = "save", arguments = { "number" })
    public void foo(int i) {
    }
}
