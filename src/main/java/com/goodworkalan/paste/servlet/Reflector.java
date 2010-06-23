package com.goodworkalan.paste.servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.goodworkalan.ilk.IlkReflect;

/**
 * An implementation of <code>IlkReflect.Refelctor</code> declared in the
 * <code>com.goodworkalan.paste.servlet</code> package in order to create
 * package private implementations of objects.
 * 
 * @author Alan Gutierrez
 */
public class Reflector extends IlkReflect.Reflector {
    /**
     * Construct a new instance of an object using the given constructor and the
     * given constructor arguments.
     * <p>
     * This method is a wrapper around <code>Constructor.newInstance</code> that
     * can create objects that are package visible to
     * <code>com.goodworkalan.paste.servlet</code>.
     * 
     * @param constructor
     *            The constructor.
     * @param arguments
     *            The constructor arguments.
     * @return A new object instance.
     */
    @Override
    public Object newInstance(Constructor<?> constructor, Object[] arguments)
    throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return constructor.newInstance(arguments);
    }
}
