package com.goodworkalan.paste.faults;

import java.util.Map;

import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.RequestScoped;

// TODO Document.
public class RaiseInvalid implements Actor
{
    // TODO Document.
    private Map<Object, Object> faults;
    
    // TODO Document.
    public RaiseInvalid(@RequestScoped @Faults Map<Object, Object> faults)
    {
        this.faults = faults;
    }

    /**
     * If there are mistakes in the data submission, raise an invalid
     */
    public Throwable actUpon(Object controller)
    {
        if (faults.size() == 0)
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
