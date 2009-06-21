package com.goodworkalan.paste;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.InstanceOf;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.goodworkalan.deviate.RuleSetBuilderList;
import com.mallardsoft.tuple.Pair;

/**
 * An element in the controller connection domain-specific language that
 * specifies which rendering module is used to render controllers, errors and
 * execptions.
 * 
 * @author Alan Gutierrez
 */
public class ViewConnector
{
    // TODO Document.
    private final ViewConnector parent;
    
    // TODO Document.
    protected final RuleMapBuilder<Pair<Integer, RenderModule>> rules;
    
    // TODO Document.
    protected final RuleSetBuilder<Pair<Integer, RenderModule>> from;
    
    // TODO Document.
    protected final List<RuleSetBuilder<Pair<Integer, RenderModule>>> listOfSetOfRules;
    
    // TODO Document.
    private int priority;
    
    // TODO Document.
    public ViewConnector(ViewConnector parent, RuleMapBuilder<Pair<Integer, RenderModule>> mapOfBindings, List<RuleSetBuilder<Pair<Integer, RenderModule>>> listOfSetOfRules) 
    {
        this.parent = parent;
        this.rules = mapOfBindings;
        this.listOfSetOfRules = listOfSetOfRules;
        this.from = listOfSetOfRules.get(0).duplicate();
    }

    // TODO Document.
    public List<RuleSetBuilder<Pair<Integer, RenderModule>>> newView()
    {
        return Collections.singletonList(new RuleSetBuilderList<Pair<Integer, RenderModule>>(listOfSetOfRules).duplicate());
    }

    // TODO Document.
    public ViewConnector view()
    {
        return new ViewConnector(this, rules, newView());
    }
    
    // TODO Document.
    public ViewConnector end()
    {
        return parent;
    }
    
    // TODO Document.
    public ViewConnector controller(Class<?> controllerClass)
    {
        listOfSetOfRules.get(0).check(BindKey.CONTROLLER_CLASS, new InstanceOf(controllerClass));
        return this;
    }
    
    public ViewConnector status(int status)
    {
        listOfSetOfRules.get(0).check(BindKey.STATUS, new Equals(status));
        return this;
    }
    
    // TODO Document.
    public ViewConnector method(String...methods)
    {
        for (String method : methods)
        {
            listOfSetOfRules.get(0).check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    // TODO Document.
    public ViewConnector exception(Class<? extends Throwable> exceptionClass)
    {
        listOfSetOfRules.get(0).check(BindKey.EXCEPTION, new Equals(exceptionClass));
        return this;
    }
    
    // TODO Document.
    public ViewConnector priority(int priority)
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
            constructor = renderClass.getConstructor(ViewConnector.class);
        }
        catch (Exception e)
        {
            throw new PasteException(e);
        }
        ViewConnector end = new ViewConnector(parent, rules, Collections.singletonList(from.duplicate()));
        T module;
        try
        {
            module = constructor.newInstance(end);
        }
        catch (Exception e)
        {
            throw new PasteException(e);
        }
        for (RuleSetBuilder<Pair<Integer, RenderModule>> setOfRules : listOfSetOfRules)
        {
            setOfRules.put(new Pair<Integer, RenderModule>(priority, module));
        }
        return module;
    }
}
