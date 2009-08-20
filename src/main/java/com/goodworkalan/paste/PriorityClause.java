package com.goodworkalan.paste;

public interface PriorityClause<T> extends ToConnector<T> {
    public ToConnector<T> priority(int priority);
}
