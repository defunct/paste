package com.goodworkalan.paste.connector;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;


/**
 * Assign the controllers to a reaction.
 * 
 * @author Alan Gutierrez
 */
public class ReactToTypeClause {
    /** The connector to return when the statement terminates. */
    private final Connector connector;
    
    /** The map of reaction types to controllers. */
    private final IlkAssociation<Class<?>> reactionsByType;
    
    /** The type key. */
    private final Ilk.Key key;

    /**
     * Create a react with clause.
     * 
     * @param connector
     *            The connector to return when the statement terminates.
     * @param reactionsByType
     *            The map of reaction types to controllers.
     * @param ilk
     *            The type key.
     */
    ReactToTypeClause(Connector connector, IlkAssociation<Class<?>> reactionsByType, Ilk.Key key) {
        this.connector = connector;
        this.reactionsByType = reactionsByType;
        this.key = key;
    }

    /**
     * Assign the controllers to a reaction.
     * 
     * @param controllerClass
     *            The controller to run in response to the event.
     * @return Return the connector to continue specifying routes and renderers.
     * 
     */
    public Connector with(Class<?> controllerClass) {
        reactionsByType.exact(key, controllerClass);
        return connector;
    }
}
