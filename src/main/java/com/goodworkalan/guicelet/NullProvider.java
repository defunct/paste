package com.goodworkalan.guicelet;

import com.google.inject.Provider;

public class NullProvider<T> implements Provider<T>
{
    public T get()
    {
        throw new UnsupportedOperationException();
    }
}
