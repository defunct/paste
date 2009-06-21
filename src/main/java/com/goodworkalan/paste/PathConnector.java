package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.MatchTest;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class PathConnector<T> implements NextRuleConnector<T>, ToConnector<T>
{
    // TODO Document.
    private final T connector;
    
    // TODO Document.
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;
    
    // TODO Document.
    private final List<GlobCompiler> compilers;
    
    // TODO Document.
    private final List<Glob> globs;
    
    // TODO Document.
    private final List<Glob> subGlobs;
    
    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, Class<?>>> rules;
    
    private final String pattern;

    // TODO Document.
    private int priority;
    
    // TODO Document.
    public PathConnector(T connector, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections, List<GlobCompiler> compilers, List<Glob> subGlobs, List<Glob> globs, RuleMapBuilder<Pair<Integer, Class<?>>> rules, String pattern)
    {
        this.subGlobs = subGlobs;
        this.connector = connector;
        this.connections = connections;
        this.compilers = compilers;
        this.globs = globs;
        this.rules = rules;
        this.pattern = pattern;
    }
    
    // TODO Document.
    public OrConnector<T> or()
    {
        for (GlobCompiler compiler : compilers)
        {
            subGlobs.add(compiler.compile(pattern));
        }
        return new OrConnector<T>(connector, connections, compilers, subGlobs, globs, rules);
    }
    
    public PathConnector<T> filtered(MatchTest matchTest)
    {
        for (GlobCompiler compiler : compilers)
        {
            compiler.test(matchTest);
        }
        return this;
    }
    
    public PathConnector<T> filter(Class<? extends MatchTest> matchTestClass)
    {
        for (GlobCompiler compiler : compilers)
        {
            compiler.test(matchTestClass);
        }
        return this;
    }
    
    public PathConnector<PathConnector<T>> path(String path)
    {
        for (GlobCompiler compiler : compilers)
        {
            subGlobs.add(compiler.compile(pattern));
        }
        List<GlobCompiler> subCompilers = new ArrayList<GlobCompiler>();
        for (Glob glob : subGlobs)
        {
            subCompilers.add(new GlobCompiler(glob));
        }
        List<Glob> globs = new ArrayList<Glob>();
        RuleMapBuilder<Pair<Integer, Class<?>>> rules = new RuleMapBuilder<Pair<Integer,Class<?>>>();
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer,Class<?>>>>(globs, rules));
        return new PathConnector<PathConnector<T>>(this, connections, subCompilers, new ArrayList<Glob>(), globs, rules, path);
    }

    /**
     * Assign a priority to current path. Followed by a <code>to</code>
     * connector assignment.
     * 
     * @param priority
     *            The priority for this path.
     * @return A connector that provides only a <code>to</code> method.
     */
    public ToConnector<T> priority(int priority)
    {
        this.priority = priority;
        return this;
    }

    /**
     * Begin adding rules based on on request properties other than the path.
     * 
     * @return An element that creates rules based on request properties other
     *         than the path.
     */
    public RuleConnector<T> when()
    {
        return new RuleConnector<T>(this, rules);
    }
    
    // TODO Document.
    public Ending<T> to(Class<?> controller)
    {
        rules.rule().put(new Pair<Integer, Class<?>>(priority, controller));
        return new Ending<T>(connector);
    }
    
    public T end()
    {
        return connector;
    }
}
