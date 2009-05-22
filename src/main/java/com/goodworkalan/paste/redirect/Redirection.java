package com.goodworkalan.paste.redirect;

import com.goodworkalan.paste.Stop;

// TODO Document.
public class Redirection extends Stop
{
    // TODO Document.
    private static final long serialVersionUID = 1L;

    // TODO Document.
    private final String where;
    
    // TODO Document.
    public Redirection(String where)
    {
        this.where = where;
    }
    
    // TODO Document.
    public void redirect(Redirector redirector)
    {
        redirector.redirect(where);
    }
}
