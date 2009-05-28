package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;


@RequestScoped
public class Response
{
    private final HttpServletResponse response;
    
    private final List<NamedValue> headers;
    
    private int status;
    
    @Inject
    public Response(HttpServletResponse response)
    {
        this.response = response;
        this.headers = new ArrayList<NamedValue>();
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
        return status;
    }
    
    public NamedValueList getHeaders()
    {
        return new NamedValueList(headers);
    }
    
    public void addHeader(String name, String value)
    {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, value));
    }
    
    public void clearHeaders(String name)
    {
        Iterator<NamedValue> eachHeader = headers.iterator();
        while (eachHeader.hasNext())
        {
            if (eachHeader.next().getName().equals(name))
            {
                eachHeader.remove();
            }
        }
    }

    // TODO Document.
    public void send()
    {
        response.setStatus(getStatus());
        for (NamedValue namedValue : headers)
        {
            response.addHeader(namedValue.getName(), namedValue.getValue());
        }
    }
}