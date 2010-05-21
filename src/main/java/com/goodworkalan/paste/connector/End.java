package com.goodworkalan.paste.connector;

/**
 * A helper element to end statement in the domain-specific language for those
 * statements that have no additional specification.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of element to return when end is called.
 */
public class End<T> {
    /** The element to return when is called. */
    private final T parent;

    /**
     * Create an ending element.
     * 
     * @param parent
     *            The element to return when is called.
     */
    End(T parent) {
        this.parent = parent;
    }

    /**
     * End a statement by returning the parent element.
     * 
     * @return The parent element to end the statement.
     */
    public T end() {
        return parent;
    }
}
