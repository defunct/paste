package com.goodworkalan.paste.servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.goodworkalan.ilk.IlkReflect;

// TODO Document.
public class Reflector extends IlkReflect.Reflector {
    // TODO Document.
    @Override
    public Object newInstance(Constructor<?> constructor, Object[] arguments)
    throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return constructor.newInstance(arguments);
    }
}
