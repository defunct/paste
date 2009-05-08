package com.goodworkalan.guicelet;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.goodworkalan.deviate.RuleSetBuilderList;

// TODO Document.
public class ViewBinder
{
    // TODO Document.
    private final ViewBinder parent;
    
    // TODO Document.
    protected final RuleMapBuilder<ViewBinding> mapOfBindings;
    
    // TODO Document.
    protected final RuleSetBuilder<ViewBinding> from;
    
    // TODO Document.
    protected final List<RuleSetBuilder<ViewBinding>> listOfSetOfRules;
    
    // TODO Document.
    private int priority;
    
    // TODO Document.
    public ViewBinder(ViewBinder parent, RuleMapBuilder<ViewBinding> mapOfBindings, List<RuleSetBuilder<ViewBinding>> listOfSetOfRules) 
    {
        this.parent = parent;
        this.mapOfBindings = mapOfBindings;
        this.listOfSetOfRules = listOfSetOfRules;
        this.from = listOfSetOfRules.get(0).duplicate();
    }

    // TODO Document.
    public List<RuleSetBuilder<ViewBinding>> newView()
    {
        if (parent == null && false)
        {
            return Collections.singletonList(mapOfBindings.rule());
        }
        return Collections.singletonList(new RuleSetBuilderList<ViewBinding>(listOfSetOfRules).duplicate());
    }

    // TODO Document.
    public ViewBinder view()
    {
        return new ViewBinder(this, mapOfBindings, newView());
    }
    
    // TODO Document.
    public ViewBinder end()
    {
        return parent;
    }
    
    // TODO Document.
    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewControllerBinder(parent, mapOfBindings, listOfSetOfRules).or(controllerClass);
    }
    
    // TODO Document.
    public ViewBinder method(String...methods)
    {
        for (String method : methods)
        {
            listOfSetOfRules.get(0).check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    // TODO Document.
    public ViewBinder exception(Class<? extends Throwable> exceptionClass)
    {
        listOfSetOfRules.get(0).check(BindKey.EXCEPTION, new Equals(exceptionClass));
        return this;
    }
    
    // TODO Document.
    public ViewBinder or()
    {
        List<RuleSetBuilder<ViewBinding>> shift = new ArrayList<RuleSetBuilder<ViewBinding>>();
        shift.add(from.duplicate());
        shift.addAll(listOfSetOfRules);
        return new ViewBinder(parent, mapOfBindings, shift);
    }
    
    // TODO Document.
    public ViewBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }

    // TODO Document.
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
