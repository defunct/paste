package com.goodworkalan.paste;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

/**
 * A basic implementation of a thread local scope that is bounded by explicit
 * function calls to enter and exit the scope.
 * <p>
 * FIXME Sort out the push and pop jiggery-pokery so that it is consistant for
 * the entire package. The safe-guards are fine, but you must define them.
 * When you are at depth zero, everything is cleared.
 * 
 * @author Alan Gutierrez
 */
public class BasicScope implements Scope
{
    /** A thread local map of type tokens to objects. */
    private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<Map<Key<?>, Object>>();
    
    /** A stack of scopes, in case we need to push and pop. */
    private final ThreadLocal<LinkedList<Map<Key<?>, Object>>> stack = new ThreadLocal<LinkedList<Map<Key<?>, Object>>>();

    /**
     * Create a basic scope.
     */
    public BasicScope()
    {
    }
    
    /**
     * Push the current scope onto a stack and set the current values to null.
     */
    public void push(Key<?>...keys)
    {
        LinkedList<Map<Key<?>,Object>> list = stack.get();
        if (list == null)
        {
            list = new LinkedList<Map<Key<?>,Object>>();
            stack.set(list);
        }
        Map<Key<?>, Object> current = values.get();
        Map<Key<?>, Object> frame = new HashMap<Key<?>, Object>();
        for (Key<?> key : keys)
        {
            frame.put(key, current.get(key));
            current.remove(key);
        }
        list.addLast(frame);
    }
    
    public void push()
    {
        LinkedList<Map<Key<?>,Object>> list = stack.get();
        if (list == null)
        {
            list = new LinkedList<Map<Key<?>,Object>>();
            stack.set(list);
        }
        list.add(values.get());
        values.set(new HashMap<Key<?>, Object>());
    }
    
    /**
     * Pop a scope off of the stack.
     */
    public void pop()
    {
        Map<Key<?>, Object> current = values.get();
        if (current == null)
        {
            current = new HashMap<Key<?>, Object>();
            values.set(current);
        }
        current.putAll(stack.get().removeLast());
    }
    
    /**
     * Enter the scope creating a new thread local hash map to store the scope
     * type token to object mappings.
     */
    public void enter()
    {
        if (values.get() == null)
        {
            values.set(new HashMap<Key<?>, Object>());
        }
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
        if (!objects.containsKey(key))
        {
            objects.put(key, value);
        }
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
