package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Assign the controllers for an annotation reaction.
 *
 * @author Alan Gutierrez
 */
public class ReactToAnnotationClause {
    /** The connector to return when the statement terminates. */
    private final Connector connector;
    
    /** The map of reaction annotations to controllers. */
    private final Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation;
    
    /** The annotation. */
    private final Class<? extends Annotation> annotationClass;

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
    ReactToAnnotationClause(Connector connector, Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation, Class<? extends Annotation> annotationClass) {
        this.connector = connector;
        this.reactionsByAnnotation = reactionsByAnnotation;
        this.annotationClass = annotationClass;
    }

    /**
     * Assign the controllers to a reaction.
     * 
     * @param controllerClass
     *            The controller to run in response to the event.
     * @return Return the connector to continue specifying routes and renderers.
     */
    public Connector with(Class<?> controllerClass) {
        List<Class<?>> controllers = reactionsByAnnotation.get(annotationClass);
        if (controllers == null) {
            controllers = new ArrayList<Class<?>>();
            reactionsByAnnotation.put(annotationClass, controllers);
        }
        controllers.add(controllerClass);
        return connector;
    }
}
