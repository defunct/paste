package com.goodworkalan.paste.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * A list of name value pairs interpreted as a list of request parameters.
 * This class adds a convenience method to interpret the list as a query
 * string. The type exists for the the sake of Guice bindings, to give
 * a more meaningful type name to the request parameters object.
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

    /**
     * Create a URL encoded query string from the list of named values.
     * 
     * @return A URL encoded query string.
     */
    public String toQueryString()
    {
        StringBuilder queryString = new StringBuilder();
        String separator = "";
        for (NamedValue namedValue : this)
        {
            queryString.append(separator);
            try
            {
                queryString.append(URLEncoder.encode(namedValue.getName(), "UTF-8"));
                queryString.append("=");
                if (namedValue.getValue() != null)
                {
                    queryString.append(URLEncoder.encode(namedValue.getValue(), "UTF-8"));
                }
            }
            catch (UnsupportedEncodingException e)
            {
            }
            separator = "&";
        }
        return queryString.toString();
    }
}
