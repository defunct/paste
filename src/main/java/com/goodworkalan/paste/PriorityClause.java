package com.goodworkalan.paste;

/**
 * The priority clause specifies the priority of a path binding to resolve ambiguities
 * when two or more path bindings match the same path.
 * 
 * @author Alan Gutierrez
 *
 * @param <T>
  *            The type of parent element to return when the statement is
 *            terminated.
 */
public interface PriorityClause<T> extends ToClause<T> {
    /**
     * Assign a priority to current path. Followed by a <code>to</code>
     * connector assignment.
     * 
     * @param priority
     *            The priority for this path.
     * @return A {@link ToClause} element to specify the connection class.
     */
    public ToClause<T> priority(int priority);
}
