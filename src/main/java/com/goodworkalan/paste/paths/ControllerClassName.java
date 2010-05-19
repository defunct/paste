package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.paste.Controller;

/**
 * A format argument that returns the currently selected controller class name.
 * 
 * @author Alan Gutierrez
 */
public class ControllerClassName {
    /** The controller. */
    private final String controllerClassName;

    /**
     * Construct a controller class name argument.
     * 
     * @param controller
     *            The currently selected controller.
     */
    @Inject
    public ControllerClassName(@Controller Object controller) {
        String name = controller.getClass().getCanonicalName();
        String pkg = controller.getClass().getPackage().getName();
        this.controllerClassName = name.substring(pkg.length() + 1).replace('.', '/');
    }
    
    /**
     * Return the controller class name.
     * 
     * @return The controller class name.
     */
    @Override
    public String toString() {
        return controllerClassName;
    }
}
