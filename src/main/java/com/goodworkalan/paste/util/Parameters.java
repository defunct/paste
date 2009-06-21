package com.goodworkalan.paste.util;

import java.util.List;

/**
 * A list of name value pairs interpreted as a list of request parameters. The
 * type exists for the the sake of Guice bindings, to give a more meaningful
 * type name to the request parameters object.
 * 
 * @author Alan Gutierrez
 */
public class Parameters extends NamedValueList
{
    /**
     * Create a list of parameters as wrapper around the given list of named
     * values.
     * 
     * @param namedValues
     *            The list of name values to copy.
     */
    public Parameters(List<NamedValue> namedValues)
    {
        super(namedValues);
    }
}
