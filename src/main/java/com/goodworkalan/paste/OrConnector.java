package com.goodworkalan.paste;

import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

//TODO Document.
public class OrConnector<T>
{
    // TODO Document.
    private final T connector;
    
    // TODO Document.
    private final Map<Class<?>, Glob> controllerToGlob;

    // TODO Document.
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;

    // TODO Document.
    private final List<Glob> globs;
    
    // TODO Document.
    private final List<GlobCompiler> compilers;

    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, Class<?>>> rules;
    
    // TODO Document.
    public OrConnector(T connector, Map<Class<?>, Glob> controllerToGlob, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections, List<GlobCompiler> compilers, List<Glob> globs, RuleMapBuilder<Pair<Integer, Class<?>>> rules)
    {
        this.connector = connector;
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
        this.compilers = compilers;
        this.globs = globs;
        this.rules = rules;
    }
    
    // TODO Document.
    public PathConnector<T> path(String path)
    {
        return new PathConnector<T>(connector, controllerToGlob, connections, compilers, globs, rules, path);
    }
}
