package com.goodworkalan.guicelet;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;

// TODO Document.
public class ControllerConditionBinder
{
    // TODO Document.
    private final ControllerPathBinder pathBinder;
    
    // TODO Document.
    protected final RuleMapBuilder<ControllerBinding> mapOfRules;
    
    // TODO Document.
    protected final RuleSetBuilder<ControllerBinding> setOfRules;
    
    // TODO Document.
    private int priority;
    
    // TODO Document.
    public ControllerConditionBinder(ControllerPathBinder pathBinder,
                                     RuleMapBuilder<ControllerBinding> mapOfRules)
    {
        this.pathBinder = pathBinder;
        this.mapOfRules = mapOfRules;
        this.setOfRules = mapOfRules.rule();
    }
    
    // TODO Document.
    public ControllerConditionBinder method(String...methods)
    {
        for (String method : methods)
        {
            setOfRules.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    // TODO Document.
    public ControllerConditionBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    // TODO Document.
    public <T> ControllerPathBinder to(Class<?> controller)
    {
        ControllerBinding binding = new ControllerBinding(priority, controller);
        setOfRules.put(binding);
        return pathBinder;
    }
}
