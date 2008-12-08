package com.goodworkalan.guicelet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.goodworkalan.deviate.Deviations;
import com.goodworkalan.deviate.Match;

public class ViewBinder
{
    private final Deviations<ViewBinding> viewBindings;
    
    public ViewBinder(Deviations<ViewBinding> viewBindings)
    {
        this.viewBindings = viewBindings;
    }

    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewConditionBinder(this, viewBindings, newPattern()).controller(controllerClass);
    }
    
    public ViewConditionBinder controller()
    {
        return new ViewConditionBinder(this, viewBindings, newPattern()).controller();
    }
    
    public Map<PatternKey, Set<Match>> newPattern()
    {
        return new HashMap<PatternKey, Set<Match>>();
    }
}
