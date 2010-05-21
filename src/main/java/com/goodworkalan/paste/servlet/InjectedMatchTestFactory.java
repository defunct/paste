package com.goodworkalan.paste.servlet;

import com.goodworkalan.dovetail.MatchTest;
import com.goodworkalan.dovetail.MatchTestFactory;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.inject.Injector;

/**
 * A Dovetail match test factory that uses the dependency injector of the Paste
 * filter to create instances of match tests. This allows Dovetail match tests
 * to be built using constructor injection to provide any of the implementations
 * available through the dependency injector of the Paste filter.
 * 
 * @author Alan Gutierrez
 * 
 */
class InjectedMatchTestFactory implements MatchTestFactory {
    /** The dependency injector of the Paste filter. */
    private final Injector injector;

    /**
     * Create a test match factory that builds test matches using the Guice
     * injector of the Paste filter.
     * 
     * @param injector
     *            The Guice injector of the Paste filter.
     */
    public InjectedMatchTestFactory(Injector injector) {
        this.injector = injector;
    }

    /**
     * Create a match test of the given class using the Guice injector of the
     * Paste filter.
     * 
     * @return A match test.
     */
    public MatchTest getInstance(Class<? extends MatchTest> matchTestClass) {
        return injector.instance(new Ilk<MatchTest>(matchTestClass), null);
    }
}
