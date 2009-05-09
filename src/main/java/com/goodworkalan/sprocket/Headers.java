package com.goodworkalan.sprocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO Document.
public class Headers
extends StringListMap
{
    // TODO Document.
    private int status;

    // TODO Document.
    private final String method;

    // TODO Document.
    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getNames(HttpServletRequest request)
    {
        return request.getHeaderNames();
    }

    // TODO Document.
    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getHeaders(HttpServletRequest request, String name)
    {
        return request.getHeaders(name);
    }

    // TODO Document.
    public static Headers fromRequest(HttpServletRequest request)
    {
        Map<String, List<String>> mapOfHeaders = new HashMap<String, List<String>>();

        Enumeration<String> names = getNames(request);
        while (names.hasMoreElements())
        {
            List<String> listOfValues = new ArrayList<String>();

            String name = names.nextElement();
            Enumeration<String> values = getHeaders(request, name);
            while (values.hasMoreElements())
            {
                listOfValues.add(values.nextElement());
            }
            
            mapOfHeaders.put(name, Collections.unmodifiableList(listOfValues));
        }

        return new Headers(mapOfHeaders, request.getMethod(), 200);
    }
    
    // TODO Document.
    public Headers(Map<String, List<String>> map, String method, int status)
    {
        super(map);
        this.method = method;
        this.status = status;
    }

    // TODO Document.
    public Headers(Map<String, List<String>> map, String method)
    {
        this(map, method, 0);
    }

    // TODO Document.
    public Headers(String method)
    {
        this(new HashMap<String, List<String>>(), method, 0);
    }
    
    // TODO Document.
    public void setStatus(int status)
    {
        if (this.status < 0)
        {
            throw new UnsupportedOperationException();
        }
        if (status < 0)
        {
            throw new IllegalArgumentException();
        }
        this.status = status;
    }
    
    // TODO Document.
    public int getStatus()
    {
        return status == Integer.MIN_VALUE ? 0 : Math.abs(status);
    }
    
    // TODO Document.
    public String getMethod()
    {
        return method;
    }

    // TODO Document.
    public void send(HttpServletResponse response)
    {
        response.setStatus(getStatus());
        for (Map.Entry<String, List<String>> header : entrySet())
        {
            for (String value : header.getValue())
            {
                response.addHeader(header.getKey(), value);
            }
        }
    }
}
