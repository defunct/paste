package com.goodworkalan.paste.audit;

import java.util.Map;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.NavigateException;
import com.goodworkalan.infuse.ParseException;
import com.goodworkalan.paste.PasteException;

// FIXME Combine with reporter.
// TODO Document.
public class Tree
{
    // TODO Document.
    private final Map<Object, Object> map;
    
    // TODO Document.
    private final Object context;
    
    // TODO Document.
    private final Object value;
    
    // TODO Document.
    public Tree(Object context, Object value, Map<Object, Object> map)
    {
        this.map = map;
        this.context = context;
        this.value = value;
    }
    
    // TODO Document.
    public Object getContext()
    {
        return context;
    }
    
    // TODO Document.
    public Object getValue()
    {
        return value;
    }
    
    // TODO Document.
    public Object getValue(String path)
    {
        Diffusion diffusion;
        try
        {
            diffusion = new Diffusion(path);
        }
        catch (ParseException e)
        {
            throw new PasteException(e);
        }
        try
        {
            return diffusion.get(map);
        }
        catch (NavigateException e)
        {
            throw new PasteException(e);
        }
    }
    
    // TODO Document.
    public Object getContextValue(String path)
    {
        Diffusion diffusion;
        try
        {
            diffusion = new Diffusion(path);
        }
        catch (ParseException e)
        {
            throw new PasteException(e);
        }
        try
        {
            return diffusion.get(context);
        }
        catch (NavigateException e)
        {
            throw new PasteException(e);
        }
    }
}
