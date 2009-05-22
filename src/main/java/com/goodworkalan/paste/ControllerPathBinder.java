package com.goodworkalan.paste;

import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;

// TODO Document.
public class ControllerPathBinder
{
    // TODO Document.
    private final List<Glob> listOfGlobs;
    
    // TODO Document.
    private final ControllerBinder binder;
    
    // TODO Document.
    private final GlobCompiler compiler;
    
    // TODO Document.
    private final RuleMapBuilder<ControllerBinding> mapOfRules;
    
    // TODO Document.
    private int priority;
    
    // TODO Document.
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
    
    // TODO Document.
    public ControllerPathBinder or(String pattern)
    {
        listOfGlobs.add(compiler.compile(pattern));
        return this;
    }

    // TODO Document.
    public ControllerPathBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    // TODO Document.
    public ControllerConditionBinder when()
    {
        return new ControllerConditionBinder(this, mapOfRules);
    }
    
    // TODO Document.
    public ControllerBinder to(Class<?> controller)
    {
        ControllerBinding binding = new ControllerBinding(priority, controller);
        mapOfRules.rule().put(binding);
        return binder;
    }
    
    // TODO Document.
    public ControllerPathBinder bind(String pattern)
    {
        return binder.bind(pattern);
    }
}
