package com.goodworkalan.guicelet;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleSetBuilder;

public class ControllerConditionBinder
{
    private final ControllerBinder binder;
    
    protected final RuleSetBuilder<ControllerBinding> rules;
    
    private int priority;
    
    public ControllerConditionBinder(ControllerBinder binder, RuleSetBuilder<ControllerBinding> rules)
    {
        this.binder = binder;
        this.rules = rules;
    }
    
    public ControllerConditionBinder method(String...methods)
    {
        for (String method : methods)
        {
            rules.check(PatternKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    public ControllerConditionBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    public <T> ControllerBinder to(Class<?> controller)
    {
        ControllerBinding binding = new ControllerBinding(priority, controller);
        rules.put(binding);
        return binder;
    }
}
