package com.goodworkalan.paste.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of name value pairs interpreted as a list of request parameters. The
 * type exists for the the sake of Guice bindings, to give a more meaningful
 * type name to the request parameters object.
 * 
 * @author Alan Gutierrez
 */
public class Parameters extends NamedValueList {
    /** The serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a list of parameters as wrapper around the given list of named
     * values.
     * 
     * @param namedValues
     *            The list of name values to copy.
     */
    public Parameters(List<NamedValue> namedValues) {
        super(namedValues);
    }

    /**
     * Create a parameter list from the given query string placing the named
     * values in the given context.
     * 
     * @param query
     *            The query string to parse.
     * @param context
     *            The context in which the parameter was specified.
     * @return A list of parameters contained in the query string.
     */
    public static Parameters fromQueryString(String query) {
        try {
            String[] parameters = query.split("&");
            List<NamedValue> namedValues = new ArrayList<NamedValue>(parameters.length);
            for (String parameter : parameters) {
                String[] pair = parameter.split("=", 2);
                String name = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length == 2) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                namedValues.add(new NamedValue(name, value));
            }
            return new Parameters(namedValues);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
