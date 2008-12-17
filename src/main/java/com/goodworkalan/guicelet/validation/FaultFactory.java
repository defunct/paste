package com.goodworkalan.guicelet.validation;

import com.goodworkalan.cassandra.Cassandra;

public class FaultFactory
{
    private final Cassandra cassandra;

    public FaultFactory(Cassandra cassandra)
    {
        this.cassandra = cassandra;
    }

    public Fault newFault(String key, String path)
    {
        return new Fault(cassandra.newMessage(key), key, path);
    }
}
