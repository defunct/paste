package com.goodworkalan.paste.controller;

/**
 * A named value list that contains HTTP request or response headers.
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
public class Headers extends NamedValueList {
    /** The default serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a list of HTTP headers by catenating the given lists of named
     * values.
     * 
     * @param namedValueLists
     *            The list of named value lists.
     */
    public Headers(NamedValueList...namedValueLists) {
        super(catenate(namedValueLists));
    }
}
