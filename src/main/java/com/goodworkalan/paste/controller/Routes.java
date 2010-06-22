package com.goodworkalan.paste.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.paste.controller.qualifiers.Application;

/**
 * Generates URL paths derived from the application routes given a controller
 * and map of parameters.
 * 
 * @author Alan Gutierrez
 */
public class Routes {
    /** Map of controller classes to Dovetail paths. */
    private final Map<Class<?>, Glob> controllerPaths;

    /**
     * Create a set of routes using the map of controllers to Dovetail paths.
     * This injected constructor will use the map controller classes to Dovetail
     * <code>Path</code> instances qualified by the {@link Application}
     * annotation.
     * 
     * @param controllerPaths
     *            The map of controllers to Dovetail paths.
     */
    @Inject
    public Routes(@Application Map<Class<?>, Glob> controllerPaths) {
        this.controllerPaths = controllerPaths;
    }

    /**
     * Get the path for the given controller class.
     * 
     * @param controllerClass
     *            The controller class.
     * @return The path for the controller.
     */
    public String path(Class<?> controllerClass) {
        return controllerPaths.get(controllerClass).path(Collections.<String, String> emptyMap());
    }

    /**
     * Get the path for the given controller class using the given map of
     * parameters to populate route parameters.
     * 
     * @param controllerClass
     *            The controller class.
     * @param parameters
     *            The parameters.
     * @return The path for the controller.
     */
    public String path(Class<?> controllerClass, Map<String, String> parameters) {
        return controllerPaths.get(controllerClass).path(parameters);
    }

    /**
     * Get the path for the given controller class use the given list of name
     * value pairs. The list of name value pairs is converted into a parameter
     * map where the string value of every object at an even index is the key
     * for the string value of the object at the subsequent odd index.
     * 
     * @param controllerClass
     *            The controller class.
     * @param parameters
     *            The list of name value pairs.
     * @return The path for the controller.
     */
    public String path(Class<?> controllerClass, Object... parameters) {
        if (parameters.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < parameters.length; i++) {
            map.put(parameters[i].toString(), parameters[i + 1].toString());
        }
        return controllerPaths.get(controllerClass).path(map);
    }
}