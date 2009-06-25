package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final Map<Class<?>, Glob> controllerToGlob;
    
    // TODO Document.
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;
    
    // TODO Document.
    private final List<GlobCompiler> compilers;
    
    // TODO Document.
    private final List<Glob> globs;
    
    // TODO Document.
    private final RuleMapBuilder<Pair<Integer, Class<?>>> rules;
    
    private final String pattern;

    // TODO Document.
    private int priority;
    
    private boolean built;
    
    // TODO Document.
    public PathConnector(T connector, Map<Class<?>, Glob> controllerToGlob, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections, List<GlobCompiler> compilers, List<Glob> globs, RuleMapBuilder<Pair<Integer, Class<?>>> rules, String pattern)
    {
        this.controllerToGlob = controllerToGlob;
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
            globs.add(compiler.compile(pattern));
        }
        built = true;
        return new OrConnector<T>(connector, controllerToGlob, connections, compilers, globs, rules);
    }
    
    // TODO Document.
    public PathConnector<T> filtered(MatchTest matchTest)
    {
        for (GlobCompiler compiler : compilers)
        {
            compiler.test(matchTest);
        }
        return this;
    }
    
    // TODO Document.
    public PathConnector<T> filter(Class<? extends MatchTest> matchTestClass)
    {
        for (GlobCompiler compiler : compilers)
        {
            compiler.test(matchTestClass);
        }
        return this;
    }
    
    // TODO Document.
    public PathConnector<PathConnector<T>> path(String path)
    {
        if (!built)
        {
            for (GlobCompiler compiler : compilers)
            {
                globs.add(compiler.compile(pattern));
            }
            built = true;
        }
        List<GlobCompiler> subCompilers = new ArrayList<GlobCompiler>();
        for (Glob glob : globs)
        {
            subCompilers.add(new GlobCompiler(glob));
        }
        List<Glob> globs = new ArrayList<Glob>();
        RuleMapBuilder<Pair<Integer, Class<?>>> rules = new RuleMapBuilder<Pair<Integer,Class<?>>>();
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer,Class<?>>>>(globs, rules));
        return new PathConnector<PathConnector<T>>(this, controllerToGlob, connections, subCompilers, new ArrayList<Glob>(), rules, path);
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
        if (!built)
        {
            for (GlobCompiler compiler : compilers)
            {
                globs.add(compiler.compile(pattern));
            }
            built = true;
        }
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer,Class<?>>>>(globs, rules));
        return new RuleConnector<T>(this, globs.get(0), controllerToGlob, rules);
    }
    
    // TODO Document.
    public Ending<T> to(Class<?> controller)
    {
        if (!built)
        {
            for (GlobCompiler compiler : compilers)
            {
                globs.add(compiler.compile(pattern));
            }
            built = true;
        }
        rules.rule().put(new Pair<Integer, Class<?>>(priority, controller));
        controllerToGlob.put(controller, globs.get(0));
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer,Class<?>>>>(globs, rules));
        return new Ending<T>(connector);
    }
    
    // TODO Document.
    public T end()
    {
        return connector;
    }
}
