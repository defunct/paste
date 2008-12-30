package com.goodworkalan.guicelet;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;
import com.goodworkalan.diverge.RuleSetBuilderList;

public class ViewConditionBinder
{
    private final ViewConditionBinder parent;
    
    protected final RuleMapBuilder<ViewBinding> mapOfBindings;
    
    protected final RuleSetBuilder<ViewBinding> from;
    
    protected final List<RuleSetBuilder<ViewBinding>> listOfSetOfRules;
    
    private int priority;
    
    public ViewConditionBinder(ViewConditionBinder parent, RuleMapBuilder<ViewBinding> mapOfBindings, List<RuleSetBuilder<ViewBinding>> listOfSetOfRules) 
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

    public ViewConditionBinder view()
    {
        return new ViewConditionBinder(this, mapOfBindings, newView());
    }
    
    public ViewConditionBinder end()
    {
        return parent;
    }
    
    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewControllerBinder(parent, mapOfBindings, listOfSetOfRules).or(controllerClass);
    }
    
    public ViewConditionBinder method(String...methods)
    {
        for (String method : methods)
        {
            listOfSetOfRules.get(0).check(PatternKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    public ViewConditionBinder exception(Class<? extends Throwable> exceptionClass)
    {
        listOfSetOfRules.get(0).check(PatternKey.EXCEPTION, new Equals(exceptionClass));
        return this;
    }
    
    public ViewConditionBinder or()
    {
        List<RuleSetBuilder<ViewBinding>> shift = new ArrayList<RuleSetBuilder<ViewBinding>>();
        shift.add(from.duplicate());
        shift.addAll(listOfSetOfRules);
        return new ViewConditionBinder(parent, mapOfBindings, shift);
    }
    
    public ViewConditionBinder priority(int priority)
    {
        this.priority = priority;
        return this;
    }

    public <T extends RenderModule> T with(Class<T> renderClass)
    {
        Constructor<T> constructor;
        try
        {
            constructor = renderClass.getConstructor(ViewConditionBinder.class);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        ViewConditionBinder end = new ViewConditionBinder(parent, mapOfBindings, Collections.singletonList(from.duplicate()));
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
