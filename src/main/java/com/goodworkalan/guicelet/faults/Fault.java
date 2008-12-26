package com.goodworkalan.guicelet.faults;

import java.util.HashMap;
import java.util.Map;

public class Fault extends HashMap<Object, Object>
{
    private static final long serialVersionUID = 1L;

    private final Map<String, Object> report;
    
    private final String message;
    
    public Fault(String message, Map<String, Object> report)
    {
        this.message = message;
        this.report = report;
    }
    
    public String getMessage()
    {
        return message;
    }

    public Map<String, Object> getReport()
    {
        return report;
    }
}