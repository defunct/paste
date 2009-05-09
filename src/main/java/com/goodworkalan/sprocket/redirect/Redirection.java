package com.goodworkalan.sprocket.redirect;

import com.goodworkalan.sprocket.Parameters;

// TODO Document.
public class Redirection extends RuntimeException
{
    // TODO Document.
    private static final long serialVersionUID = 1L;

    // TODO Document.
    private final String where;
    
    // TODO Document.
    private final Parameters parameters;
    
    // TODO Document.
    public Redirection(String where)
    {
        this.where = where;
        this.parameters = new Parameters();
    }
    
    // TODO Document.
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
    
    // TODO Document.
    public Redirection parameter(String name, String value)
    {
        parameters.add(name, value);
        return this;
    }
}
