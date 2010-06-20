package com.goodworkalan.paste.controller;

import java.util.Map;

// TODO Document.
public interface Routes {
    // TODO Document.
    public String path(Class<?> controllerClass);

    // TODO Document.
    public String path(Class<?> controllerClass, Map<String, String> parameters);

    // TODO Document.
    public String path(Class<?> controllerClass, Object... parameters);
}