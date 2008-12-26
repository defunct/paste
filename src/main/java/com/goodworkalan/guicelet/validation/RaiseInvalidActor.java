package com.goodworkalan.guicelet.validation;

import java.util.Map;

import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.RequestScoped;

public class RaiseInvalidActor implements Actor
{
    private final Map<String, Fault> faults;
    
    public RaiseInvalidActor(@RequestScoped @Faults Map<String, Fault> faults)
    {
        this.faults = faults;
    }

    public void actUpon(Object controller)
    {
        if (faults.size() != 0)
        {
            throw new Invalid();
        }
    }
}
