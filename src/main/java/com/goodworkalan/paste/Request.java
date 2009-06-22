package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.NamedValueList;
import com.google.inject.Inject;

// TODO Document.
@RequestScoped
public class Request
{
    // TODO Document.
    private final HttpServletRequest request;
    
    // TODO Document.
    private final NamedValueList headers;
    
    // TODO Document.
    @Inject
    public Request(HttpServletRequest request)
    {
        this.headers = fromHttpServerRequest(request);
        this.request = request;
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
    private static NamedValueList fromHttpServerRequest(HttpServletRequest request)
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
        
        return new NamedValueList(headers);
    }
    
    // TODO Document.
    public Map<String, String[]> getParamters()
    {
        return getParameterMap(request);
    }

    // TODO Document.
    @SuppressWarnings("unchecked")
    public static Map<String, String[]> getParameterMap(HttpServletRequest request)
    {
        return request.getParameterMap();
    }

    // TODO Document.
    public String getMethod()
    {
        return request.getMethod();
    }
    
    // TODO Document.
    public String getContextPath()
    {
        return request.getContextPath();
    }
    
    // TODO Document.
    public String getRequestURI()
    {
        return request.getRequestURI();
    }
    
    // TODO Document.
    public String getRequestURL()
    {
        return request.getRequestURL().toString();
    }

    // TODO Document.
    public NamedValueList getHeaders()
    {
        return headers;
    }
}