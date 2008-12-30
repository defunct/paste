package com.goodworkalan.guicelet;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;

public class ControllerConditionBinder
{
    private final ControllerPathBinder pathBinder;
    
    protected final RuleMapBuilder<ControllerBinding> mapOfRules;
    
    protected final RuleSetBuilder<ControllerBinding> setOfRules;
    
    private int priority;
    
    public ControllerConditionBinder(ControllerPathBinder pathBinder,
                                     RuleMapBuilder<ControllerBinding> mapOfRules)
    {
        this.pathBinder = pathBinder;
        this.mapOfRules = mapOfRules;
        this.setOfRules = mapOfRules.rule();
    }
    
    public ControllerConditionBinder method(String...methods)
    {
        for (String method : methods)
        {
            setOfRules.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    public ControllerConditionBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    public <T> ControllerPathBinder to(Class<?> controller)
    {
        ControllerBinding binding = new ControllerBinding(priority, controller);
        setOfRules.put(binding);
        return pathBinder;
    }
}
