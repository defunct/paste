package com.goodworkalan.paste.controller;

import java.util.Map;

/**
 * Generates URL paths derived from the application routes given a controller
 * and map of parameters.
 * 
 * @author Alan Gutierrez
 */
public interface Routes {
    /**
     * Get the path for the given controller class.
     * 
     * @param controllerClass
     *            The controller class.
     * @return The path for the controller.
     */
    public String path(Class<?> controllerClass);

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
    public String path(Class<?> controllerClass, Map<String, String> parameters);

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
    public String path(Class<?> controllerClass, Object... parameters);
}