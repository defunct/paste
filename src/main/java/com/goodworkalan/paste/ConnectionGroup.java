package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class ConnectionGroup
{
    // TODO Document.
    private Map<Class<?>, Glob> controllerToGlob;
    
    /** A list of globs to rule mappings. */
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;
    
    // TODO Document.
    public ConnectionGroup(Map<Class<?>, Glob> controllerToGlob, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections)
    {
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
    }

    // TODO Document.
    public PathConnector<ConnectionGroup> path(String path)
    {
        return new PathConnector<ConnectionGroup>(this, controllerToGlob, connections, Collections.singletonList(new GlobCompiler()), new ArrayList<Glob>(), new RuleMapBuilder<Pair<Integer,Class<?>>>(), path);
    }

    /**
     * Terminate the domain-specific language connection statement.
     */
    public void end()
    {
    }
}
