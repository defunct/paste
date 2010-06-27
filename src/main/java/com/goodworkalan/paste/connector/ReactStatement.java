package com.goodworkalan.paste.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A builder for specifying an event that is not directly associated with an
 * HTTP request. This is used by timers to implement delayed jobs or background
 * tasks. Reaction controllers are built using dependency injection, so that
 * application scoped resources are available during the reaction.
 * 
 * @author Alan Gutierrez
 * 
 * @see com.goodworkalan.paste.controller.Reactor
 */
public class ReactStatement {
    /** The connector to return when the statement terminates. */
    private final Connector connector;
    
    /** The map of types to controllers. */
    private final Map<Class<?>, List<Class<?>>> reactions;

    /**
     * Create a reaction statement.
     * 
     * @param connector
     *            The connector to return when the statement terminates.
     * @param reactions
     *            The map of types to controllers.
     */
    ReactStatement(Connector connector, Map<Class<?>, List<Class<?>>> reactions) {
        this.connector = connector;
        this.reactions = reactions;
    }
    
    // TODO Document.
    public ReactWithClause to(Class<?>...triggers) {
        List<List<Class<?>>> assignments = new ArrayList<List<Class<?>>>();
        for (Class<?> trigger : triggers) {
            List<Class<?>> controllers = reactions.get(trigger);
            if (controllers == null) {
                controllers = new ArrayList<Class<?>>();
                reactions.put(trigger, controllers);
            }
            assignments.add(controllers);
        }
        return new ReactWithClause(connector, assignments);
    }
}
