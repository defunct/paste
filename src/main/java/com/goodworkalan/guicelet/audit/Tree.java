package com.goodworkalan.guicelet.audit;

import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.GuiceletException;

public class Tree
{
    private final Map<Object, Object> map;
    
    private final Object context;
    
    private final Object value;
    
    public Tree(Object context, Object value, Map<Object, Object> map)
    {
        this.map = map;
        this.context = context;
        this.value = value;
    }
    
    public Object getContext()
    {
        return context;
    }
    
    public Object getValue()
    {
        return value;
    }
    
    public Object getValue(String path)
    {
        PropertyPath property;
        try
        {
            property = new PropertyPath(path);
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
        try
        {
            return property.get(map);
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
    }
    
    public Object getContextValue(String path)
    {
        PropertyPath property;
        try
        {
            property = new PropertyPath(path);
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
        try
        {
            return property.get(context);
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
    }
}
