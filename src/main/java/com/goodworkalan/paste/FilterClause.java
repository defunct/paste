package com.goodworkalan.paste;

import com.goodworkalan.dovetail.MatchTest;

public interface FilterClause<T> extends SubPathClause<T> {
    public FilterClause<T> filtered(MatchTest matchTest);
    
    public FilterClause<T> filter(Class<? extends MatchTest> matchTestClass);
}
