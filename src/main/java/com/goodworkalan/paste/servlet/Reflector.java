package com.goodworkalan.paste.servlet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.goodworkalan.ilk.IlkReflect;

public class Reflector extends IlkReflect.Reflector {
    @Override
    public Object newInstance(Constructor<?> constructor, Object[] arguments)
    throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return constructor.newInstance(arguments);
    }
}
