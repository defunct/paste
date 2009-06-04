package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.inject.Inject;

// FIXME Headers that appear here should also appear in our HttpServletResponse. 
@RequestScoped
public class Response
{
    private final List<NamedValue> headers;
    
    private int status;
    
    @Inject
    public Response()
    {
        this.status = 200;
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
    
    public void addHeader(String name, int value)
    {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, Integer.toString(value)));
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

}