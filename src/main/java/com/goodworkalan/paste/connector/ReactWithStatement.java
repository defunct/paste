package com.goodworkalan.paste.connector;

import java.util.List;


/**
 * A statement in the domain-specific routing language that maps an annotation
 * to a controller, for use with application specific reactions.
 * 
 * @author Alan Gutierrez
 */
public class ReactWithStatement {
    /** The parent connector language element. */
    private final Connector connector;
    
    // TODO Document.
    private final List<List<Class<?>>> assignments;

    // TODO Document.
    ReactWithStatement(Connector connector, List<List<Class<?>>> assignments, Class<?>...triggers) {
        this.connector = connector;
        this.assignments = assignments;
    }
    
    // TODO Document.
    public Connector with(Class<?>...controllers) {
        for (List<Class<?>> assign : assignments) {
            for (Class<?> controller : controllers) {
                assign.add(controller);
            }
        }
        return connector;
    }
}
