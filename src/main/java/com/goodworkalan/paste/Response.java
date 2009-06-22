package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.NamedValueList;
import com.google.inject.Inject;

// FIXME Headers that appear here should also appear in our HttpServletResponse. 
@RequestScoped
public class Response
{
    // TODO Document.
    private final List<NamedValue> headers;
    
    // TODO Document.
    private int status;
    
    // TODO Document.
    @Inject
    public Response()
    {
        this.status = 200;
        this.headers = new ArrayList<NamedValue>();
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
        return status;
    }
    
    // TODO Document.
    public NamedValueList getHeaders()
    {
        return new NamedValueList(headers);
    }
    
    // TODO Document.
    public void addHeader(String name, int value)
    {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, Integer.toString(value)));
    }
    
    // TODO Document.
    public void addHeader(String name, String value)
    {
        headers.add(new NamedValue(NamedValue.RESPONSE, name, value));
    }
    
    // TODO Document.
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