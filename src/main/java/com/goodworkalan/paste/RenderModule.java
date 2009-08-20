package com.goodworkalan.paste;

import com.google.inject.AbstractModule;

/**
 * A base class for all render module implementations that allows render modules
 * to act as extensions of the domain-specific binding langauge.
 *
 * @author Alan Gutierrez
 */
public abstract class RenderModule extends AbstractModule {
    /** The connector to return when the statement terminates. */
    private final Connector end;

    /**
     * Construct a render module with the given conector that will be returned
     * when the statement terminates.
     * 
     * @param end
     *            The connector to return when the statement terminates.
     */
    public RenderModule(Connector end) {
        this.end = end;
    }

    /**
     * Terminate the statement by returning the connctor.
     * 
     * @return The connector to specify other renderers and paths.
     */
    public Connector end() {
        return end;
    }
}
