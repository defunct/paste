package com.goodworkalan.guicelet.audit;

import java.util.HashMap;
import java.util.Map;

public class CoreReport implements Report
{
    Map<String, Object> map = new HashMap<String, Object>();

    public String getKeyStem()
    {
        return null;
    }
    
    public Report put(String name, Object object)
    {
        return this;
    }
    
    public void report(String name)
    {
    }
}
