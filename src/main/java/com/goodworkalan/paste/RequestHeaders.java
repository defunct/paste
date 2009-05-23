package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * An immutable list of named values that represents the list of HTTP request
 * headers.
 * 
 * @author Alan Gutierrez
 */
public class RequestHeaders extends NamedValueList
{
    /**
     * Create a list of request headers from the given list of named values.
     * 
     * @param namedValues
     *            The underlying list of named values.
     */
    public RequestHeaders(List<NamedValue> namedValues)
    {
        super(namedValues);
    }

    /**
     * Cast the header values in the given HTTP servlet request to a string
     * enumeration.
     * 
     * @param request
     *            The HTTP servlet request.
     * @return A string enumeration.
     */
    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getNames(HttpServletRequest request)
    {
        return request.getHeaderNames();
    }

    /**
     * Cast the header values for the given header name in the given HTTP
     * servlet request to a string enumeration.
     * 
     * @param request
     *            The HTTP servlet request.
     * @param name
     *            The header name.
     * @return A string enumeration.
     */
    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getHeaders(HttpServletRequest request, String name)
    {
        return request.getHeaders(name);
    }

    /**
     * Create a list of request headers from the request headers in the given
     * HTTP servlet request.
     * 
     * @param request
     *            The HTTP servlet request.
     * @return A list of request headers.
     */
    public static RequestHeaders fromHttpServerRequest(HttpServletRequest request)
    {
        List<NamedValue> headers = new ArrayList<NamedValue>();

        Enumeration<String> names = getNames(request);
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            Enumeration<String> values = getHeaders(request, name);
            while (values.hasMoreElements())
            {
                headers.add(new NamedValue(NamedValue.REQUEST, name, values.nextElement()));
            }
        }
        
        return new RequestHeaders(headers);
    }
}
