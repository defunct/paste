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
    
    private final Map<String, List<String>> headers;
    
    @SuppressWarnings("unchecked")
    public Headers(HttpServletRequest request)
    {
        Map<String, List<String>> mapOfHeaders = new HashMap<String, List<String>>();

        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements())
        {
            List<String> listOfValues = new ArrayList<String>();

            String name = names.nextElement();
            Enumeration<String> values = request.getHeaders(name);
            while (values.hasMoreElements())
            {
                listOfValues.add(values.nextElement());
            }
            
            mapOfHeaders.put(name, Collections.unmodifiableList(listOfValues));
        }

        status = 200; // It is OK, is it not?
        method = request.getMethod();
        headers = Collections.unmodifiableMap(mapOfHeaders);
    }
    
    public Headers(String theMethod)
    {
        method = theMethod;
        headers = new HashMap<String, List<String>>();
    }
    
    public void setStatus(int newStatus)
    {
        if (status != 0)
        {
            throw new IllegalStateException();
        }
        status = newStatus;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public String getMethod()
    {
        return method;
    }

    public void send(HttpServletResponse response)
    {
        response.setStatus(getStatus());
        for (Map.Entry<String, List<String>> header : headers.entrySet())
        {
            for (String value : header.getValue())
            {
                response.addHeader(header.getKey(), value);
            }
        }
    }

    public void add(String name, String value)
    {
        List<String> values = headers.get(name);
        if (values == null)
        {
            values = new ArrayList<String>();
            headers.put(name, values);
        }
        values.add(value);
    }
    
    public void clear()
    {
        headers.clear();
    }
    
    public void clear(String name)
    {
        headers.remove(name);
    }
    
    public boolean contains(String name)
    {
        return headers.containsKey(name);
    }
    
    public void remove(String name, String value)
    {
        if (headers.containsKey(name))
        {
            headers.get(name).remove(value);
        }
    }
    
    public String get(String name)
    {
        if (headers.containsKey(name))
        {
            return headers.get(name).get(0);
        }
        return null;
    }
}
