package com.goodworkalan.paste;

import com.google.inject.Provider;

/**
 * A dummy provider for properties that are explicity set into the scope.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T> The type to provide.
 */
public class NullProvider<T> implements Provider<T> {
    /**
     * This method should never be called.
     * 
     * @return Nothing, ever.
     */
    public T get() {
        throw new UnsupportedOperationException();
    }
}
