package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Parameters
{
    private final Map<String, List<String>> parameters;
    
    @SuppressWarnings("unchecked")
    public Parameters(HttpServletRequest request) 
    {
        Map<String, List<String>> copy = new HashMap<String, List<String>>();
        Map<String, String[]> source = request.getParameterMap();
        for (Map.Entry<String, String[]> parameter : source.entrySet())
        {
            List<String> values = new ArrayList<String>();
            for (String value : parameter.getValue())
            {
                values.add(value);
            }
            copy.put(parameter.getKey(), values);
        }
        parameters = copy;
    }
    
    public Parameters(Map<String, List<String>> parameters)
    {
        this.parameters = parameters;
    }
    
    public final static Parameters fromMapOfStrings(Map<String, String> mapOfStrings)
    {
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        for (Map.Entry<String, String> entry : mapOfStrings.entrySet())
        {
            List<String> values = new ArrayList<String>();
            values.add(entry.getValue());
            parameters.put(entry.getKey(), values);
        }
        return new Parameters(parameters);
    }
    
    public Parameters()
    {
        this.parameters = new HashMap<String, List<String>>();
    }
    
    public Parameters unmodifiable()
    {
        Map<String, List<String>> unmodifiable = new HashMap<String, List<String>>();
        for (Map.Entry<String, List<String>> parameter : parameters.entrySet())
        {
            unmodifiable.put(parameter.getKey(), Collections.unmodifiableList(new ArrayList<String>(parameter.getValue())));
        }
        return new Parameters(Collections.unmodifiableMap(unmodifiable));
    }
    
    public String get(String name)
    {
        if (parameters.containsKey(name))
        {
            return parameters.get(name).get(0);
        }
        return null;
    }
}
