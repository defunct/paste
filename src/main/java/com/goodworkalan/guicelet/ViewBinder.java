package com.goodworkalan.guicelet;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.goodworkalan.deviate.RuleSetBuilderList;

public class ViewBinder
{
    private final ViewBinder parent;
    
    protected final RuleMapBuilder<ViewBinding> mapOfBindings;
    
    protected final RuleSetBuilder<ViewBinding> from;
    
    protected final List<RuleSetBuilder<ViewBinding>> listOfSetOfRules;
    
    private int priority;
    
    public ViewBinder(ViewBinder parent, RuleMapBuilder<ViewBinding> mapOfBindings, List<RuleSetBuilder<ViewBinding>> listOfSetOfRules) 
    {
        this.parent = parent;
        this.mapOfBindings = mapOfBindings;
        this.listOfSetOfRules = listOfSetOfRules;
        this.from = listOfSetOfRules.get(0).duplicate();
    }

    public List<RuleSetBuilder<ViewBinding>> newView()
    {
        if (parent == null && false)
        {
            return Collections.singletonList(mapOfBindings.rule());
        }
        return Collections.singletonList(new RuleSetBuilderList<ViewBinding>(listOfSetOfRules).duplicate());
    }

    public ViewBinder view()
    {
        return new ViewBinder(this, mapOfBindings, newView());
    }
    
    public ViewBinder end()
    {
        return parent;
    }
    
    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewControllerBinder(parent, mapOfBindings, listOfSetOfRules).or(controllerClass);
    }
    
    public ViewBinder method(String...methods)
    {
        for (String method : methods)
        {
            listOfSetOfRules.get(0).check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    public ViewBinder exception(Class<? extends Throwable> exceptionClass)
    {
        listOfSetOfRules.get(0).check(BindKey.EXCEPTION, new Equals(exceptionClass));
        return this;
    }
    
    public ViewBinder or()
    {
        List<RuleSetBuilder<ViewBinding>> shift = new ArrayList<RuleSetBuilder<ViewBinding>>();
        shift.add(from.duplicate());
        shift.addAll(listOfSetOfRules);
        return new ViewBinder(parent, mapOfBindings, shift);
    }
    
    public ViewBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }

    public <T extends RenderModule> T with(Class<T> renderClass)
    {
        Constructor<T> constructor;
        try
        {
            constructor = renderClass.getConstructor(ViewBinder.class);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        ViewBinder end = new ViewBinder(parent, mapOfBindings, Collections.singletonList(from.duplicate()));
        T module;
        try
        {
            module = constructor.newInstance(end);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        for (RuleSetBuilder<ViewBinding> setOfRules : listOfSetOfRules)
        {
            setOfRules.put(new ViewBinding(priority, module));
        }
        return module;
    }
}
