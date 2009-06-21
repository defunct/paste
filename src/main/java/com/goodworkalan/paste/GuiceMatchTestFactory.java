package com.goodworkalan.paste;

import com.goodworkalan.dovetail.MatchTest;
import com.goodworkalan.dovetail.MatchTestFactory;
import com.google.inject.Injector;

public class GuiceMatchTestFactory implements MatchTestFactory
{
    private final Injector injector;

    public GuiceMatchTestFactory(Injector injector)
    {
        this.injector = injector;
    }
    
    public MatchTest getInstance(Class<? extends MatchTest> matchTestClass)
    {
        return injector.getInstance(matchTestClass);
    }
}
