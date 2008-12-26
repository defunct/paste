package com.goodworkalan.guicelet.faults;

import java.util.Map;

import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.RequestScoped;

public class RaiseInvalid implements Actor
{
    private Map<Object, Object> faults;
    
    public RaiseInvalid(@RequestScoped @Faults Map<Object, Object> faults)
    {
        this.faults = faults;
    }

    public void actUpon(Object controller)
    {
        if (faults.size() == 0)
        {
            throw new Invalid();
        }
    }
}
