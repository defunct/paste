package com.goodworkalan.paste;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * Implementation of the Guice application scope.
 * <p>
 * This implementation stores objects in a concurrent hash map, but uses a
 * synchronized block to populate the hash map. If the object it not found in
 * the concurrent map, a synchronized block is entered so that the get method of
 * the unscoped provider will be called only once.
 * 
 * @author Alan Gutierrez
 */
class ApplicationScope implements Scope {
    /** The map. */
    private final ConcurrentMap<Key<?>, Object> map = new ConcurrentHashMap<Key<?>, Object>();

    /**
     * Get the instance associated with the given key, creating it with the
     * given unscoped provider if it does not exist.
     * 
     * @param key
     *            The key.
     * @param unscoped
     *            The unscoped provider.
     */
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            public T get() {
                @SuppressWarnings("unchecked")
                T t = (T) map.get(key);
                if (t == null) {
                    synchronized (this) {
                        @SuppressWarnings("unchecked")
                        T current = (T) map.get(key);
                        if (current == null) {
                            t = unscoped.get();
                            map.put(key, t);
                        } else {
                            t = current;
                        }
                    }
                }
                return t;
            }
        };
    }
}
