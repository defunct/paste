package com.goodworkalan.paste.controller;

import java.util.Collections;

/**
 * A named value list that contains request parameters.
 * <p>
 * This subclass exists for the sake of disambiguation between the
 * parameters map available through dependency injection. We could
 * create a special qualifier for the headers and parameters.
 * 
 * <pre><code>
 * public void doSomething(@RequestHeaders NamedValue, @RequestParameters NamedValue) { 
 * }
 * </code></pre>
 * 
 * However, that is not as succinct as the following.
 * 
 * <pre><code>
 * public void doSomething(@Request Headers, @Request Parameters) { 
 * }
 * </code></pre>
 *
 * @author Alan Gutierrez
 */
public class Parameters extends NamedValueList {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create an empty set of parameters.
     */
    public Parameters() {
        super(Collections.<NamedValue>emptyList());
    }
    
    /**
     * Create a list of request parameters by combining the given lists of named
     * values.
     * 
     * @param namedValueLists
     *            The list of named value lists.
     */
    public Parameters(NamedValueList... namedValueLists) {
        super(catenate(namedValueLists));
    }
}
