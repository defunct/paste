package com.goodworkalan.paste;

public interface NextRuleConnector<T>
{
    public RuleConnector<T> when();
    
    public T end();
}
