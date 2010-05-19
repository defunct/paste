package com.goodworkalan.paste;

import javax.inject.Provider;

/**
 * A dummy provider for properties that are explicitly set into the scope.
 * <p>
 * Note that in the Google Guice scopes examples, they use a singleton class
 * that implements <code>Provider&lt;Object%gt;</code>. We can't do that because
 * it does not work with parameterized values for <code>T</code>.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type to provide.
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
