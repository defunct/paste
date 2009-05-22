package com.goodworkalan.paste;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * A basic implementation of a thread local scope that is bounded by explicit
 * function calls to enter and exit the scope.
 * 
 * @author Alan Gutierrez
 */
public class BasicScope implements Scope
{
    /** A thread local map of type tokens to objects. */
    private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<Map<Key<?>, Object>>();

    /**
     * Create a basic scope.
     */
    public BasicScope()
    {
    }
    
    /**
     * Enter the scope creating a new thread local hash map to store the scope
     * type token to object mappings.
     */
    public void enter()
    {
        values.set(new HashMap<Key<?>, Object>());
    }

    /**
     * Leave the scope deleting the thread local hash map to store the scope
     * type token to object mappings.
     */
    public void exit()
    {
        values.remove();
    }

    /**
     * Seed the scope by explicitly mapping the given type token to the given
     * object.
     * 
     * @param <T>
     *            The type of the mapping.
     * @param key
     *            The type token us as a binding key.
     * @param value
     *            The object to map to the type token.
     */
    public <T> void seed(Key<T> key, T value)
    {
        Map<Key<?>, Object> objects = values.get();
        objects.put(key, value);
    }

    /**
     * Seed the scope by explicitly mapping the given class to the given object.
     * 
     * @param <T>
     *            The type of the mapping.
     * @param clazz
     *            The class to use as a binding key.
     * @param value
     *            The object to map to the type token.
     */
    public <T> void seed(Class<T> clazz, T value)
    {
        seed(Key.get(clazz), value);
    }

    /**
     * Scopes a provider. The returned provider returns objects from this scope.
     * If an object does not exist in this scope, the given unscoped provider is
     * used to retrieve one.
     * 
     * @param key
     *            The binding key.
     * @param unscoped
     *            Use to locate an instance if one does not already exist in 
     *            the scope.
     * @return A new provider which only delegates to the given unscoped
     *         provider when an instance of the requested object doesn't already
     *         exist in this scope.
     */
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped)
    {
        return new Provider<T>()
        {
            public T get()
            {
                Map<Key<?>, Object> objects = values.get();

                @SuppressWarnings("unchecked")
                T current = (T) objects.get(key);
                if (current == null && !objects.containsKey(key))
                {
                    current = unscoped.get();
                    objects.put(key, current);
                }
                return current;
            }
            
            @Override
            public String toString()
            {
                return unscoped.toString();
            }
        };
    }
}
