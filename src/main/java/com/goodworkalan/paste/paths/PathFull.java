package com.goodworkalan.paste.paths;

import javax.inject.Inject;

import com.goodworkalan.paste.controller.Criteria;

/**
 * A format argument that returns the full path of the request URI.
 * 
 * @author Alan Gutierrez
 */
public class PathFull {
    /** The full path of the request URI. */
    private final String argument;
    
    /**
     * Construct a full path format argument that will extract the path from
     * the given criteria.
     * 
     * @param criteria
     *            The criteria.
     */
    @Inject
    public PathFull(Criteria criteria) {
        this.argument = criteria.getPath();
    }

    /**
     * Get the full path of the request URI.
     * 
     * @return The full path of the request URI.
     */
    @Override
    public String toString() {
        return argument;
    }
}
