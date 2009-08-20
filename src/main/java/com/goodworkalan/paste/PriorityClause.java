package com.goodworkalan.paste;

public interface PriorityClause<T> extends ToClause<T> {
    public ToClause<T> priority(int priority);
}
