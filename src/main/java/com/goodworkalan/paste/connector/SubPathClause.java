package com.goodworkalan.paste.connector;

/**
 * The sub-path clause of a path statement specifies sub-paths to the currently
 * defined path or paths.
 * 
 * @author alan
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public interface SubPathClause<T> extends WhenClause<T>, PriorityClause<T> {
    /**
     * Begin a sub-path statement whose path is a sub-path of all the paths
     * specified by this path statement.
     * 
     * @return A path language element to specify a sub-path.
     */
    public PathStatement<SubPathClause<T>> path(String path);
}
