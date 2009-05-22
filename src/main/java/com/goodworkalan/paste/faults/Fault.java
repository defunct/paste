package com.goodworkalan.paste.faults;

import java.util.HashMap;
import java.util.Map;

// TODO Document.
/**
 * Maybe rename Mistake or Goof. Then Fault can be used for Abnormality.
 */
public class Fault extends HashMap<Object, Object>
{
    // TODO Document.
    private static final long serialVersionUID = 1L;

    // TODO Document.
    private final Map<String, Object> report;
    
    // TODO Document.
    private final String message;
    
    // TODO Document.
    public Fault(String message, Map<String, Object> report)
    {
        this.message = message;
        this.report = report;
    }
    
    // TODO Document.
    public String getMessage()
    {
        return message;
    }

    // TODO Document.
    public Map<String, Object> getReport()
    {
        return report;
    }
}