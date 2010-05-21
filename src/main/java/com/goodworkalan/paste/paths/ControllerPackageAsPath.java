package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.paste.qualifiers.Controller;

/**
 * A format argument that returns the currently selected controller package
 * as a file path.
 * 
 * @author Alan Gutierrez
 */
public class ControllerPackageAsPath {
    /** The controller package as path. */
    private final String argument;

    /**
     * Construct a controller package as path argument.
     * 
     * @param controller
     *            The currently selected controller.
     */
    @Inject
    public ControllerPackageAsPath(@Controller Object controller) {
        this.argument = controller.getClass().getPackage().getName().replace('.', '/');
    }

    /**
     * Get the controller package as path.
     * 
     * @return The controller package as path.
     */
    @Override
    public String toString() {
        return argument;
    }
}
