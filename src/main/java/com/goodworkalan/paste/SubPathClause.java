package com.goodworkalan.paste;

public interface SubPathClause<T> extends NextRuleConnector<T>, PriorityClause<T> {
    public PathConnector<SubPathClause<T>> path(String path);
}
