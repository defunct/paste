package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.paste.controller.Criteria;

/**
 * A format argument that returns the file part of the request URI of the filter
 * invocation.
 * 
 * @author Alan Gutierrez
 */
public class PathFile {
    /** The file part of the request URI. */
    private final String argument;

    /**
     * Construct a request file name argument that will extract the path from
     * the given criteria and use the given welcome file if the path ends with a
     * slash. You can create a Guice module to bind the welcome file name
     * suitable for your application.
     * 
     * @param criteria
     *            The criteria.
     * @param welcome
     *            The welcome file for a directory.
     */
    @Inject
    public PathFile(Criteria criteria, @WelcomeFile String welcome) {
        String path = criteria.getPath();
        if (path.length() < 2) {
            this.argument = welcome;
        } else {
            int toothpick = path.lastIndexOf('/');
            if (toothpick != -1 && toothpick + 1 < path.length()) {
                this.argument = path.substring(toothpick + 1);
            } else {
                this.argument = welcome;
            }
        }
    }

    /**
     * Get the file part of the request URI.
     * 
     * @return The file part of the request URI.
     */
    @Override
    public String toString() {
        return argument;
    }
}
