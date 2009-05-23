package com.goodworkalan.paste;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

// TODO Document.
public class ResponseHeaders
extends NamedValueList
{
    /** The HTTP return status. */
    private int status;

    private final List<NamedValue> namedValues;

    // TODO Document.
    public ResponseHeaders(List<NamedValue> namedValues)
    {
        super(namedValues);
        this.namedValues = namedValues;
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
