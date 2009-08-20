package com.goodworkalan.paste;

public interface SubPathClause<T> extends WhenClause<T>, PriorityClause<T> {
    public PathStatement<SubPathClause<T>> path(String path);
}
