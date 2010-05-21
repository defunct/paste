package com.goodworkalan.paste.connector;


/**
 * A language element that specifies a controller for a path that has not
 * specified rules using the when statement.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public interface ToClause<T> {
    /**
     * Bind the current path to the given controller, returning a terminal
     * laguage element.
     * 
     * @param controller
     *            The controller bound to this path.
     */
    public End<T> to(Class<?> controller);
}
