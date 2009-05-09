package com.goodworkalan.sprocket.faults;

import java.util.Map;

import com.goodworkalan.sprocket.Actor;
import com.goodworkalan.sprocket.RequestScoped;

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

    // TODO Document.
    public void actUpon(Object controller)
    {
        if (faults.size() == 0)
        {
            throw new Invalid();
        }
    }
}
