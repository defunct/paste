package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class CoreConnector implements Connector
{
    // TODO Document.
    private Map<Class<?>, Glob> controllerToGlob;
    
    // TODO Document.
    private final List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections;
    
    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, RenderModule>> viewRules;
    
    // TODO Document.
    public CoreConnector(Map<Class<?>, Glob> controllerToGlob, List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections, RuleMapBuilder<Pair<Integer, RenderModule>> viewRules)
    {
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
        this.viewRules = viewRules;
    }

    // TODO Document.
    public ConnectionGroup connect()
    {
        List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> group = new ArrayList<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>();
        connections.add(group);
        return new ConnectionGroup(controllerToGlob, group);
    }
    
    // TODO Document.
    public ViewConnector view()
    {
        return new ViewConnector(null, viewRules, Collections.singletonList(viewRules.rule()));
    }
}
