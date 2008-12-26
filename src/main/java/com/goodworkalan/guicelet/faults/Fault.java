package com.goodworkalan.guicelet.faults;

import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.cassandra.Message;

public class Fault
{
    private final Message message;
    
    private final String key;
    
    private final String path;
    
    private final Map<String, String> map = new HashMap<String, String>();
    
    public Fault(Message message, String key, String path)
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
    
    public Message getMessage()
    {
        return message;
    }

    public Map<String, String> getMap()
    {
        return map;
    }
}