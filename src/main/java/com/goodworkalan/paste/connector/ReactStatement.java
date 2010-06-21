package com.goodworkalan.paste.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// TODO Document.
public class ReactStatement {
    /** The parent connector language element. */
    private final Connector connector;
    
    /** The map of annotations to controllers. */
    private final Map<Class<?>, List<Class<?>>> reactions;
    
    // TODO Document.
    ReactStatement(Connector connector, Map<Class<?>, List<Class<?>>> reactions) {
        this.connector = connector;
        this.reactions = reactions;
    }
    
    // TODO Document.
    public ReactWithStatement to(Class<?>...triggers) {
        List<List<Class<?>>> assignments = new ArrayList<List<Class<?>>>();
        for (Class<?> trigger : triggers) {
            List<Class<?>> controllers = reactions.get(trigger);
            if (controllers == null) {
                controllers = new ArrayList<Class<?>>();
                reactions.put(trigger, controllers);
            }
            assignments.add(controllers);
        }
        return new ReactWithStatement(connector, assignments);
    }
}
