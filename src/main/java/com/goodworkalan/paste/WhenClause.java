package com.goodworkalan.paste;

/**
 * An element in the domain specific language used to specify additional
 * criteria based on request properties. The next rule connector is provided to
 * a rule connector so that the rule connector can return this next rule
 * connector when the rule statement terminates. This provides for a switch/case
 * like statement to further winnow the choice of controller after the path has
 * matched.
 * <p>
 * A path statement can terminate with a to statement or a when statement, but
 * not both. Once a when statement is used, the to statement is not visible. If
 * you do want a default statement, use when but specify no criteria or
 * priority.
 * <p>
 * The next rule connector interface is implemented by the path connector and
 * provided to a rule connector so that when the rule connector terminates, it
 * can return this rule connector interface to specify an additional rule based
 * path connector, while preventing the creation of sub-paths. In this way, the
 * specification of sub-paths is forced to come before the specification of
 * rules. Forced, so long as the caller uses method chaining and does not save
 * elements into variables.
 * <p>
 * Note that the class is parameterized in order to determine what type of
 * element to return at the end of the statement. Once you use a when statement
 * then you are no longer able to terminate the current path with a to
 * statement. You must use a when statement to map all controllers. The next
 * rule element must allow you to create rule statements and must also allow you
 * to terminate the path statement. Thus, the next rule connector is
 * parameterized to return whatever a path connector can return.
 * 
 * @author Alan Gutierrez
 */
public interface WhenClause<T> {
    /**
     * Create a new set of rules rules based on on request properties other than
     * the path.
     * 
     * @return An element that creates rules based on request properties other
     *         than the path.
     */
    public WhenStatement<T> when();

    /**
     * Return the parent element of the path connector associated with this rule
     * statement switch.
     * 
     * @return The parent element of the path element.
     */
    public T end();
}
