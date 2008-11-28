package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;

public class ControllerPathBinder
{
    private final GlobTree<ControllerBinding> treeOfBindings;
    
    private final Glob glob;
    
    private final ControllerBinder controllerBinder;
    
    private final Map<String, Class<? extends Annotation>> mapOfAnnotations;
    
    private final List<ControllerCondition> listOfConditions;
    
    private int priority;
    
    public ControllerPathBinder(ControllerBinder controllerBinder,
            GlobTree<ControllerBinding> treeOfBindings, Glob glob)
    {
        this.glob = glob;
        this.treeOfBindings = treeOfBindings;
        this.controllerBinder = controllerBinder;
        this.mapOfAnnotations = new HashMap<String, Class<? extends Annotation>>();
        this.listOfConditions = new ArrayList<ControllerCondition>();
    }

    public ControllerPathBinder map(String string, Class<? extends Annotation> annotationType)
    {
        mapOfAnnotations.put(string, annotationType);
        return this;
    }
    
    public ControllerPathBinder forMethod(String... method)
    {
        listOfConditions.add(new ForMethods(method));
        return this;
    }
    
    public ControllerPathBinder withPriority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    public ControllerBinder to(Class<?> controller)
    {
        treeOfBindings.add(glob, new ControllerBinding(listOfConditions, priority, controller));
        return controllerBinder;
    }
}
