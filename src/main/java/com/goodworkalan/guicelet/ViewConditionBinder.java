package com.goodworkalan.guicelet;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.deviate.Any;
import com.goodworkalan.deviate.Deviations;
import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.Match;

public class ViewConditionBinder
{
    protected final ViewBinder viewBinder;
    
    protected final Deviations<ViewBinding> viewBindings;
    
    protected final Map<PatternKey, Set<Match>> pattern;
    
    public ViewConditionBinder(ViewBinder viewBinder, Deviations<ViewBinding> viewBindings, Map<PatternKey, Set<Match>> pattern)
    {
        this.viewBinder = viewBinder;
        this.viewBindings = viewBindings;
        this.pattern = pattern;
    }
    
    protected void add(PatternKey key, Match match)
    {
        Set<Match> matches = pattern.get(key);
        if (matches == null)
        {
            matches = new HashSet<Match>();
            pattern.put(key, matches);
        }
        matches.add(match);
    }

    public ViewConditionBinder controller()
    {
        add(PatternKey.CONTROLLER, new Any());
        return this;
    }

    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewControllerBinder(viewBinder, viewBindings, pattern).or(controllerClass);
    }
    
    public ViewConditionBinder forMethod(String method)
    {
        add(PatternKey.METHOD, new Equals(method));
        return this;
    }
    
    public ViewConditionBinder forMethods(String...methods)
    {
        for (String method : methods)
        {
            add(PatternKey.METHOD, new Equals(method));
        }
        return this;
    }

    public <T extends RenderModule> T with(Class<T> binderClass)
    {
        Constructor<T> constructor;
        try
        {
            constructor = binderClass.getConstructor(ViewBinder.class);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        T module;
        try
        {
            module = constructor.newInstance(viewBinder);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        return module;
    }
}
