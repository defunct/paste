package com.goodworkalan.guicelet;

import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;

public class ControllerPathBinder
{
    private final List<Glob> listOfGlobs;
    
    private final ControllerBinder binder;
    
    private final GlobCompiler compiler;
    
    private final RuleMapBuilder<ControllerBinding> mapOfRules;
    
    private int priority;
    
    public ControllerPathBinder(ControllerBinder binder,
                                List<Glob> listOfGlobs,
                                RuleMapBuilder<ControllerBinding> mapOfRules,
                                GlobCompiler compiler)
    {
        this.compiler = compiler;
        this.listOfGlobs = listOfGlobs;
        this.binder = binder;
        this.mapOfRules = mapOfRules;
    }
    
    public ControllerPathBinder or(String pattern)
    {
        listOfGlobs.add(compiler.compile(pattern));
        return this;
    }

    public ControllerPathBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    public ControllerConditionBinder when()
    {
        return new ControllerConditionBinder(this, mapOfRules);
    }
    
    public ControllerBinder to(Class<?> controller)
    {
        ControllerBinding binding = new ControllerBinding(priority, controller);
        mapOfRules.rule().put(binding);
        return binder;
    }
    
    public ControllerPathBinder bind(String pattern)
    {
        return binder.bind(pattern);
    }
}
