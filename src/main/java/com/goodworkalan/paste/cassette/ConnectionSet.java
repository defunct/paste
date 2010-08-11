package com.goodworkalan.paste.cassette;

import java.util.ArrayList;

/**
 * A structure around an association used to store connection definitions that
 * adds a flag indicating whether or not to include the suffix when comparing a
 * path to the set of path matches. The associative container is either a
 * <code>List</code> of <code>Connection</code> structures created by the
 * domain-specific language, or a Dovetail <code>PathAsssociation</code> built
 * from the domain-specific language definitions.
 * <p>
 * The motivation here is to create a generic structure that can be used in both
 * the builder and product of the builder. The reader might find this to be a
 * trifling use of generics, but I say that it represents the notion of a
 * connection set in the two stages of its life.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The associative container type.
 */
public class ConnectionSet<T> extends ArrayList<T> {
    /** The serial id. */
    private static final long serialVersionUID = 1L;
    
    /** Whether or not to include the suffix when matching the path. */
    public boolean includeSuffix;
    
    /** The associative container for connection paths to controller. */
    public final T association;

    /**
     * Create a connection set.
     * 
     * @param connections
     *            The associative container for connection paths to controller.
     */
    public ConnectionSet(T connections) {
        this.association = connections;
    }
}
