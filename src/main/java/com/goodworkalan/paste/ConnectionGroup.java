package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class ConnectionGroup
{
    /** A list of globs to rule mappings. */
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;
    
    // TODO Document.
    public ConnectionGroup(List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections)
    {
        this.connections = connections;
    }

    // TODO Document.
    public PathConnector<ConnectionGroup> path(String path)
    {
        List<Glob> globs = new ArrayList<Glob>();
        RuleMapBuilder<Pair<Integer, Class<?>>> rules = new RuleMapBuilder<Pair<Integer,Class<?>>>();
        return new PathConnector<ConnectionGroup>(this, connections, Collections.singletonList(new GlobCompiler()), new ArrayList<Glob>(), globs, rules, path);
    }

    /**
     * Terminate the domain-specific language connection statement.
     */
    public void end()
    {
    }
}
