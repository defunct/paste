package com.goodworkalan.guicelet.validation;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.cassandra.Cassandra;

public class Fault
{
    private final Cassandra.Message message;
    
    private final String key;
    
    private final String path;
    
    private final Map<String, String> map = new HashMap<String, String>();
    
    public Fault(Cassandra.Message message, String key, String path)
    {
        this.message = message;
        this.key = key;
        this.path = path;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public String getPath()
    {
        return path;
    }
    
    public Cassandra.Message getMessage()
    {
        return message;
    }

    public Map<String, String> getMap()
    {
        return map;
    }
}