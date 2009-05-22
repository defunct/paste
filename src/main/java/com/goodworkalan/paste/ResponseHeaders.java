package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO Document.
public class ResponseHeaders
extends NamedValueList
{
    // TODO Document.
    private int status;

    // TODO Document.
    private final String method;
    
    private final List<NamedValue> namedValues;

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
    public static NamedValueList fromRequest(HttpServletRequest request)
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
    public ResponseHeaders(List<NamedValue> namedValues, String method)
    {
        super(namedValues);
        this.namedValues = namedValues;
        this.method = method;
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
    
    public void add(String name, String value)
    {
        namedValues.add(new NamedValue(NamedValue.RESPONSE, name, value));
    }

    // TODO Document.
    public void send(HttpServletResponse response)
    {
        response.setStatus(getStatus());
        for (NamedValue namedValue : namedValues)
        {
            response.addHeader(namedValue.getName(), namedValue.getValue());
        }
    }
}
