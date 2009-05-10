package com.goodworkalan.sprocket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.GlobTree;
import com.google.inject.Module;

// TODO Document.
public class CoreBinder implements Binder
{
    // TODO Document.
    private final List<List<ControllerPathMapping>> listOfControllerBinders;
    
    // TODO Document.
    private final RuleMapBuilder<ViewBinding> mapOfRules;
    
    // TODO Document.
    public CoreBinder()
    {
        this.listOfControllerBinders = new ArrayList<List<ControllerPathMapping>>();
        this.mapOfRules = new RuleMapBuilder<ViewBinding>();
    }

    // TODO Document.
    public ControllerBinder controllers(Class<?> conditional)
    {
        List<ControllerPathMapping> listOfControllerPathMappings = new ArrayList<ControllerPathMapping>();
        listOfControllerBinders.add(listOfControllerPathMappings);
        return new ControllerBinder(new GlobCompiler(conditional), listOfControllerPathMappings);
    }
    
    // TODO Document.
    public ViewBinder view()
    {
        return new ViewBinder(null, mapOfRules, Collections.singletonList(mapOfRules.rule()));
    }
    
    // TODO Document.
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
    
    // TODO Document.
    public RuleMap<ViewBinding> getMapOfRules()
    {
        return mapOfRules.newRuleMap();
    }
    
    // TODO Document.
    public SprocketGuicer newGuiceletGuicer(List<Module> modules)
    {
        return new SprocketGuicer(getBindingTrees(), getMapOfRules(), modules);
    }
}
