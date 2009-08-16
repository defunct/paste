package com.goodworkalan.paste;

import com.goodworkalan.dovetail.MatchTest;
import com.goodworkalan.dovetail.MatchTestFactory;
import com.google.inject.Injector;

/**
 * A Dovetail match test factory that uses the Guice injector of the Paste
 * filter to create instances of match tests. This allows Dovetail match tests
 * to be built using constructor injection to provide any of the implementations
 * available through the Guice injector of the Paste filter.
 * 
 * @author Alan Gutierrez
 * 
 */
public class GuiceMatchTestFactory implements MatchTestFactory {
    /** The Guice injector of the Paste filter. */
    private final Injector injector;

    /**
     * Create a test match factory that builds test matches using the Guice
     * injector of the Paste filter.
     * 
     * @param injector
     *            The Guice injector of the Paste filter.
     */
    public GuiceMatchTestFactory(Injector injector) {
        this.injector = injector;
    }

    /**
     * Create a match test of the given class using the Guice injector of the
     * Paste filter.
     * 
     * @return A match test.
     */
    public MatchTest getInstance(Class<? extends MatchTest> matchTestClass) {
        return injector.getInstance(matchTestClass);
    }
}
