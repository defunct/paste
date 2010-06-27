package com.goodworkalan.paste.connector;

import java.util.List;


/**
 * Assign the controllers to a reaction.
 * 
 * @author Alan Gutierrez
 */
public class ReactWithClause {
    /** The connector to return when the statement terminates. */
    private final Connector connector;
    
    /** The controllers to run in response to the event. */
    private final List<List<Class<?>>> assignments;

    /**
     * Create a react with clause.
     * 
     * @param connector
     *            The connector to return when the statement terminates.
     * @param assignments
     *            The controllers to run in response to the event.
     */
    ReactWithClause(Connector connector, List<List<Class<?>>> assignments, Class<?>...triggers) {
        this.connector = connector;
        this.assignments = assignments;
    }

    /**
     * Assign the controllers to a reaction.
     * 
     * @param controllers
     *            The controllers to run in response to the event.
     * @return Return the connector to continue specifying routes and renderers.
     * 
     */
    public Connector with(Class<?>...controllers) {
        for (List<Class<?>> assign : assignments) {
            for (Class<?> controller : controllers) {
                assign.add(controller);
            }
        }
        return connector;
    }
}
