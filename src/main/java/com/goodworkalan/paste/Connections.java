package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

// TODO Document.
class Connections
{
    // TODO Document.
    private Map<Class<?>, Glob> controllerToGlob;
    
    // TODO Document.
    private final List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections;
    
    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, RenderModule>> viewRules;
    
    // TODO Document.
    public Connections()
    {
        this.controllerToGlob = new HashMap<Class<?>, Glob>();
        this.connections = new ArrayList<List<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>>();
        this.viewRules = new RuleMapBuilder<Pair<Integer,RenderModule>>();
    }
    
    // TODO Document.
    public Connector newConnector()
    {
        return new CoreConnector(controllerToGlob, connections, viewRules);
    }
    
    public Routes getRoutes()
    {
        return new Routes(controllerToGlob);
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
}
