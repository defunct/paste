package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;
import com.google.inject.Module;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

// TODO Document.
public class CoreConnector implements Connector
{
    // TODO Document.
    private final List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections;
    
    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, RenderModule>> viewRules;
    
    // TODO Document.
    public CoreConnector()
    {
        this.connections = new ArrayList<List<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>>();
        this.viewRules = new RuleMapBuilder<Pair<Integer,RenderModule>>();
    }

    // TODO Document.
    public ConnectionGroup connect()
    {
        List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> group = new ArrayList<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>();
        connections.add(group);
        return new ConnectionGroup(group);
    }
    
    // TODO Document.
    public ViewConnector view()
    {
        return new ViewConnector(null, viewRules, Collections.singletonList(viewRules.rule()));
    }
    
    // TODO Document.
    public List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> getBindingTrees()
    {
        List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> trees = new ArrayList<GlobTree<RuleMap<Pair<Integer, Class<?>>>>>();
        for (List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> listOfControllerPathMappings : connections)
        {
            GlobTree<RuleMap<Pair<Integer, Class<?>>>> tree = new GlobTree<RuleMap<Pair<Integer, Class<?>>>>(); 
            for (Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>> mapping : listOfControllerPathMappings)
            {
                RuleMap<Pair<Integer, Class<?>>> rules = Tuple.get2(mapping).newRuleMap();
                for (Glob glob : Tuple.get1(mapping))
                {
                    tree.add(glob, rules);
                }
            }
            trees.add(tree);
        }
        return trees;
    }
    
    // TODO Document.
    public RuleMap<Pair<Integer, RenderModule>> getViewRules()
    {
        return viewRules.newRuleMap();
    }
    
    // TODO Document.
    public PasteGuicer newGuiceletGuicer(List<Module> modules, ServletContext servletContext, Map<String, String> initialization)
    {
        return new PasteGuicer(getBindingTrees(), getViewRules(), modules, servletContext, initialization);
    }
}
