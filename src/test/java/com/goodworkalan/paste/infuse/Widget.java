package com.goodworkalan.paste.infuse;

import java.util.Map;

public class Widget
{
    private Map<String, Map<String, String>> stringMapMap; 
    
    private String string;
    
    public void setString(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }
    
    public void setStringMapMap(Map<String, Map<String, String>> stringMapMap)
    {
        this.stringMapMap = stringMapMap;
    }
    
    public Map<String, Map<String, String>> getStringMapMap()
    {
        return stringMapMap;
    }
    
    public void setString(String a, String b, String value)
    {
        stringMapMap.get(a).put(b, value);
    }
    
    public String getString(String a, String b)
    {
        return stringMapMap.get(a).get(b);
    }
}
