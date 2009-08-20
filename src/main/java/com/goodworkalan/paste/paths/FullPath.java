package com.goodworkalan.paste.paths;

import com.goodworkalan.paste.Criteria;
import com.google.inject.Inject;

/**
 * A format argument that returns the full path of the request URI.
 * 
 * @author Alan Gutierrez
 */
public class FullPath {
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
    public FullPath(Criteria criteria) {
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
