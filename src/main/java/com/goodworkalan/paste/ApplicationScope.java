package com.goodworkalan.paste;

import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

class ApplicationScope implements Scope {
    private final Map<Key<?>, Object> map;
    
    public ApplicationScope(Map<Key<?>, Object> map) {
        this.map = map;
    }
    
    /**
     * Scopes a provider Servlet context.
     */
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            public T get() {
                synchronized (map) {
                    @SuppressWarnings("unchecked")
                    T t = (T) map.get(key);
                    if (t == null) {
                        t = unscoped.get();
                        map.put(key, t);
                    }
                    return t;
                }
            }
        };
    }
}
