package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

public class ConnectionGroup
{
    private final Connector connector;

    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;
    
    public ConnectionGroup(Connector connector, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections)
    {
        this.connector = connector;
        this.connections = connections;
    }

    // TODO Document.
    public PathConnector<ConnectionGroup> path(String path)
    {
        List<Glob> globs = new ArrayList<Glob>();
        RuleMapBuilder<Pair<Integer, Class<?>>> rules = new RuleMapBuilder<Pair<Integer,Class<?>>>();
        return new PathConnector<ConnectionGroup>(this, connections, Collections.singletonList(new GlobCompiler()), new ArrayList<Glob>(), globs, rules, path);
    }
    
    public Connector end()
    {
        return connector;
    }
}
