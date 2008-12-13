package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Headers
extends StringListMap
{
    private int status;

    private final String method;

    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getNames(HttpServletRequest request)
    {
        return request.getHeaderNames();
    }

    @SuppressWarnings("unchecked")
    private final static Enumeration<String> getHeaders(HttpServletRequest request, String name)
    {
        return request.getHeaders(name);
    }

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
    
    public Headers(Map<String, List<String>> map, String method, int status)
    {
        super(map);
        this.method = method;
        this.status = status;
    }

    public Headers(Map<String, List<String>> map, String method)
    {
        this(map, method, 0);
    }

    public Headers(String method)
    {
        this(new HashMap<String, List<String>>(), method, 0);
    }
    
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
    
    public int getStatus()
    {
        return status == Integer.MIN_VALUE ? 0 : Math.abs(status);
    }
    
    public String getMethod()
    {
        return method;
    }

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
