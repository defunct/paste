package com.goodworkalan.paste;

public interface ToConnector<T>
{
    public Ending<T> to(Class<?> controller);
}
