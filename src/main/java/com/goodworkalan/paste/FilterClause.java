package com.goodworkalan.paste;

import com.goodworkalan.dovetail.MatchTest;

/**
 * A filter clause specifies Dovetail match filters to apply against the results
 * of a matched path.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public interface FilterClause<T> extends SubPathClause<T> {
    /**
     * Specify a Dovetail match filter instance to apply against any matched
     * paths.
     * 
     * @param matchTest
     *            A match filter instance.
     * @return A filter clause to continue to specify match filters or to
     *         specify rules.
     */
    public FilterClause<T> filter(MatchTest matchTest);

    /**
     * Specify a Dovetail match filter class to apply against any matched paths.
     * The match filter class will be constructed by the same Guice injector
     * used to construct controllers. The match filter can take advantage of the
     * servlet, request and filter scopes.
     * 
     * @param matchTestClass
     *            A match filter class.
     * @return A filter clause to continue to specify match filters or to
     *         specify rules.
     */
    public FilterClause<T> filter(Class<? extends MatchTest> matchTestClass);
}
