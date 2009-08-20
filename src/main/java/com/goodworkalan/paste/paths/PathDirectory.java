package com.goodworkalan.paste.paths;

import com.goodworkalan.paste.Criteria;
import com.google.inject.Inject;

/**
 * A format argument that returns the directory part of the request URI of the
 * filter invocation.
 * 
 * @author Alan Gutierrez
 */
public class PathDirectory {
    /** The directory part of the request URI. */
    private final String argument;

    /**
     * Create a format argument that returns the directory part of the request
     * URI of the filter invocation.
     * 
     * @param criteria
     *            The criteria.
     */
    @Inject
    public PathDirectory(Criteria criteria) {
        String path = criteria.getPath();
        if (path.length() == 0) {
            this.argument = "";
        } else {
            this.argument = path.substring(0, path.lastIndexOf('/'));
        }
    }

    /**
     * Get the directory part of the request URI.
     * 
     * @return The directory part of the request URI.
     */
    @Override
    public String toString() {
        return argument;
    }
}
