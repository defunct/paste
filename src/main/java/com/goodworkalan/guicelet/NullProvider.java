package com.goodworkalan.guicelet;

import com.google.inject.Provider;

// TODO Document.
public class NullProvider<T> implements Provider<T>
{
    // TODO Document.
    public T get()
    {
        throw new UnsupportedOperationException();
    }
}
