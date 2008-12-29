package com.goodworkalan.guicelet;

import java.lang.reflect.Constructor;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;

public class ViewConditionBinder
{
    protected final ViewBinder binder;
    
    protected final RuleMapBuilder<ViewBinding> mapOfBindings;
    
    protected final RuleSetBuilder<ViewBinding> setOfRules;
    
    private int priority;
    
    public ViewConditionBinder(ViewBinder binder, RuleMapBuilder<ViewBinding> mapOfBindings, RuleSetBuilder<ViewBinding> setOfRules) 
    {
        this.binder = binder;
        this.mapOfBindings = mapOfBindings;
        this.setOfRules = setOfRules;
    }
    
    public ViewControllerBinder controller(Class<?> controllerClass)
    {
        return new ViewControllerBinder(binder, mapOfBindings, setOfRules).or(controllerClass);
    }
    
    public ViewConditionBinder forMethod(String method)
    {
        setOfRules.check(PatternKey.METHOD, new Equals(method));
        return this;
    }
    
    public ViewConditionBinder forMethods(String...methods)
    {
        for (String method : methods)
        {
            setOfRules.check(PatternKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    public ViewConditionBinder withPriority(int priority)
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
        T module;
        try
        {
            module = constructor.newInstance(binder);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        setOfRules.put(new ViewBinding(priority, module));
        return module;
    }
}
