package com.goodworkalan.guicelet;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.diverge.RuleMap;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;
import com.google.inject.Injector;

public class CoreBinder implements Binder
{
    private final List<List<ControllerPathMapping>> listOfControllerBinders;
    
    private final RuleMapBuilder<ViewBinding> viewBindings;
    
    public CoreBinder()
    {
        this.listOfControllerBinders = new ArrayList<List<ControllerPathMapping>>();
        this.viewBindings = new RuleMapBuilder<ViewBinding>();
    }

    public ControllerBinder controllers(Class<?> conditional)
    {
        List<ControllerPathMapping> listOfControllerPathMappings = new ArrayList<ControllerPathMapping>();
        listOfControllerBinders.add(listOfControllerPathMappings);
        return new ControllerBinder(new GlobCompiler(conditional), listOfControllerPathMappings);
    }
    
    public ViewBinder view()
    {
        return new ViewBinder(viewBindings);
    }
    
    public List<GlobTree<RuleMap<ControllerBinding>>> getBindingTrees()
    {
        List<GlobTree<RuleMap<ControllerBinding>>> trees = new ArrayList<GlobTree<RuleMap<ControllerBinding>>>();
        for (List<ControllerPathMapping> listOfControllerPathMappings : listOfControllerBinders)
        {
            GlobTree<RuleMap<ControllerBinding>> tree = new GlobTree<RuleMap<ControllerBinding>>(); 
            for (ControllerPathMapping mapping : listOfControllerPathMappings)
            {
                RuleMap<ControllerBinding> rules = mapping.rules.newRuleMap();
                for (Glob glob : mapping.listOfGlobs)
                {
                    tree.add(glob, rules);
                }
            }
            trees.add(tree);
        }
        return trees;
    }
    
    public RuleMap<ViewBinding> getViewBindings()
    {
        return viewBindings.newRuleMap();
    }
    
    public GuiceletGuicer newGuiceletGuicer(Injector injector)
    {
        return new GuiceletGuicer(injector, getBindingTrees(), getViewBindings());
    }
}
