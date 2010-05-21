package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.paste.controller.qualifiers.Controller;

/**
 * A format argument that returns the currently selected controller cannoical
 * class name as a file path.
 * 
 * @author Alan Gutierrez
 */
public class ControllerClassAsPath {
    /** The controller cannoical class name as path. */
    private final String argument;

    /**
     * Construct a controller cannoical class name as path argument.
     * 
     * @param controller
     *            The currently selected controller.
     */
    @Inject
    public ControllerClassAsPath(@Controller Object controller) {
        this.argument = controller.getClass().getCanonicalName().replace('.', '/');
    }

    /**
     * Get the controller cannoical class name as path.
     * 
     * @return The controller cannoical class name as path.
     */
    @Override
    public String toString() {
        return argument;
    }
}
