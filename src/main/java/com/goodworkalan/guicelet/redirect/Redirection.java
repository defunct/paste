package com.goodworkalan.guicelet.redirect;

import com.goodworkalan.guicelet.Parameters;

public class Redirection extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final String where;
    
    private final Parameters parameters;
    
    public Redirection(String where)
    {
        this.where = where;
        this.parameters = new Parameters();
    }
    
    public void redirect(Redirector redirector)
    {
        for (String name : parameters.keySet())
        {
            for (String value : parameters.get(name))
            {
                redirector.parameter(name, value);
            }
        }
        redirector.redirect(where);
    }
    
    public Redirection parameter(String name, String value)
    {
        parameters.add(name, value);
        return this;
    }
}
