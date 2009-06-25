package com.goodworkalan.paste;

import com.goodworkalan.dovetail.MatchTest;
import com.goodworkalan.dovetail.MatchTestFactory;
import com.google.inject.Injector;

public class GuiceMatchTestFactory implements MatchTestFactory
{
    private final ScopeManager scopeManager;
    
    private final Injector injector;

    public GuiceMatchTestFactory(ScopeManager scopeManager, Injector injector)
    {
        this.scopeManager = scopeManager;
        this.injector = injector;
    }
    
    public MatchTest getInstance(Class<? extends MatchTest> matchTestClass)
    {
        scopeManager.enterRequest();
        return injector.getInstance(matchTestClass);
    }
}
