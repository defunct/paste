package com.goodworkalan.guicelet;

import java.util.Map;
import java.util.Set;

import com.goodworkalan.deviate.Deviations;
import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.Match;

public class ViewControllerBinder extends ViewConditionBinder
{
    public ViewControllerBinder(ViewBinder viewBinder, Deviations<ViewBinding> viewBindings,  Map<PatternKey, Set<Match>> pattern)
    {
        super(viewBinder, viewBindings, pattern);
    }
    
    public ViewControllerBinder or(Class<?> controllerClass)
    {
        add(PatternKey.CONTROLLER, new Equals(controllerClass));
        return this;
    }
}
