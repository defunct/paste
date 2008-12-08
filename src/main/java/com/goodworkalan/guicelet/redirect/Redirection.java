package com.goodworkalan.guicelet.redirect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Redirection extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final String where;
    
    private final Map<String, List<String>> parameters;
    
    public Redirection(String where)
    {
        this.where = where;
        this.parameters = new HashMap<String, List<String>>();
    }
    
    public void redirect(Redirector redirector)
    {
        if (where != null)
        {
            redirector.redirect(where);
        }
        for (String name : parameters.keySet())
        {
            for (String value : parameters.get(name))
            {
                redirector.parameter(name, value);
            }
        }
    }
    
    public Redirection parameter(String name, String value)
    {
        List<String> values = parameters.get(name);
        if (values == null)
        {
            values = new ArrayList<String>();
            parameters.put(name, values);
        }
        values.add(value);
        return this;
    }
}
