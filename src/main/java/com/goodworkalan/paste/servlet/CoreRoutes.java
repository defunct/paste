package com.goodworkalan.paste.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.paste.controller.Routes;

// TODO Document.
public class CoreRoutes implements Routes  {
    // TODO Document.
    private final Map<Class<?>, Glob> controllerToGlob;

    // TODO Document.
    public CoreRoutes(Map<Class<?>, Glob> controllerToGlob) {
        this.controllerToGlob = controllerToGlob;
    }

    // TODO Document.
    /* (non-Javadoc)
     * @see com.goodworkalan.paste.X#path(java.lang.Class)
     */
    public String path(Class<?> controllerClass) {
        return controllerToGlob.get(controllerClass).path(Collections.<String, String> emptyMap());
    }

    // TODO Document.
    /* (non-Javadoc)
     * @see com.goodworkalan.paste.X#path(java.lang.Class, java.util.Map)
     */
    public String path(Class<?> controllerClass, Map<String, String> parameters) {
        return controllerToGlob.get(controllerClass).path(parameters);
    }

    // TODO Document.
    /* (non-Javadoc)
     * @see com.goodworkalan.paste.X#path(java.lang.Class, java.lang.Object)
     */
    public String path(Class<?> controllerClass, Object... parameters) {
        if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < parameters.length; i++) {
            map.put(parameters[i].toString(), parameters[i + 1].toString());
        }
        return controllerToGlob.get(controllerClass).path(map);
    }
}
