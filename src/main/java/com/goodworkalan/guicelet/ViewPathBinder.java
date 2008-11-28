package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;

public class ViewPathBinder
{
    private final GlobTree<ViewBinding> treeOfBindings;

    private final Glob glob;

    private final ViewBinder viewBinder;

    private final List<ViewCondition> listOfConditions;

    private int priority;

    public ViewPathBinder(ViewBinder viewBinder, GlobTree<ViewBinding> treeOfBindings, Glob glob)
    {
        this.viewBinder = viewBinder;
        this.treeOfBindings = treeOfBindings;
        this.listOfConditions = new ArrayList<ViewCondition>();
        this.glob = glob;
    }

    public ViewPathBinder forBundle(Class<? extends Annotation> bundles)
    {
        listOfConditions.add(new ForBundles(bundles));
        return this;
    }

    public ViewPathBinder forNames(String...names)
    {
        listOfConditions.add(new ForNames(names));
        return this;
    }

    public ViewPathBinder withPriority(int priority)
    {
        this.priority = priority;
        return this;
    }

    public <T extends RenderModule> T to(Class<T> binderClass)
    {
        Constructor<T> constructor;
        try
        {
            constructor = binderClass.getConstructor(ViewBinder.class);
        }
        catch (SecurityException e)
        {
            throw new GuiceletException(e);
        }
        catch (NoSuchMethodException e)
        {
            throw new GuiceletException(e);
        }
        T module;
        try
        {
            module = constructor.newInstance(viewBinder);
        }
        catch (Exception e)
        {
            throw new GuiceletException(e);
        }
        treeOfBindings.add(glob, new ViewBinding(listOfConditions, priority, module));
        return module;
    }
}
