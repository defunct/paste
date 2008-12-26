package com.goodworkalan.guicelet.validation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.RequestScoped;

public class PostTree
{
    private final Map<String, Object> tree;

    public PostTree(@RequestScoped @Tree Map<String, Object> tree)
    {
        this.tree = tree;
    }

    @SuppressWarnings("unchecked")
    public Map<Object, Object> getMap(String pattern)
    {
        return (Map) getObject(pattern);
    }

    public Object getObject(String pattern)
    {
        try
        {
            PropertyPath path = new PropertyPath(pattern);
            return path.get(tree);
        }
        catch (PathException e)
        {
            return null;
        }
    }

    public String getString(String pattern)
    {
        try
        {
            PropertyPath path = new PropertyPath(pattern);
            return (String) path.get(tree);
        }
        catch (PathException e)
        {
            return null;
        }
    }

    public List<String> find(String pattern)
    {
        return Collections.emptyList();
    }
}
