package com.goodworkalan.paste.validate;

import com.goodworkalan.paste.Actor;

// TODO Document.
public class RaiseInvalid implements Actor
{
    // TODO Document.
    private Mistakes mistakes;
    
    // TODO Document.
    public RaiseInvalid(Mistakes mistakes)
    {
        this.mistakes = mistakes;
    }

    /**
     * If there are mistakes in the data submission, raise an invalid
     */
    public Throwable actUpon(Object controller)
    {
        if (mistakes.size() > 0)
        {
            try
            {
                throw new Invalid();
            }
            catch (Invalid e)
            {
                return e;
            }
        }
        return null;
    }
}
