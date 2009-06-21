package com.goodworkalan.paste;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class RuleConnector<T>
{
    // TODO Document.
    private final NextRuleConnector<T> nextRuleConnector;
    
    // TODO Document.
    protected final RuleMapBuilder<Pair<Integer, Class<?>>> rules;
    
    // TODO Document.
    protected final RuleSetBuilder<Pair<Integer, Class<?>>> rule;
    
    // TODO Document.
    private int priority;
    
    // TODO Document.
    public RuleConnector(PathConnector<T> nextRuleConnector, RuleMapBuilder<Pair<Integer, Class<?>>> rules)
    {
        this.nextRuleConnector = nextRuleConnector;
        this.rules = rules;
        this.rule = rules.rule();
    }
    
    // TODO Document.
    public RuleConnector<T> method(String...methods)
    {
        for (String method : methods)
        {
            rule.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }
    
    // TODO Document.
    public RuleConnector<T> priority(int priority)
    {
        this.priority = priority;
        return this;
    }
    
    // TODO Document.
    public Ending<NextRuleConnector<T>> to(Class<?> controller)
    {
        rule.put(new Pair<Integer, Class<?>>(priority, controller));
        return new Ending<NextRuleConnector<T>>(nextRuleConnector);
    }
}
