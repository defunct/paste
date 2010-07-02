package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;

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
    
    /** The map of reaction types to controllers. */
    private final IlkAssociation<Class<?>> reactionsByType;

    /** The reaction annotations to controllers. */
    private final Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation;

    /**
     * Create a reaction statement.
     * 
     * @param connector
     *            The connector to return when the statement terminates.
     * @param reactionsByType
     *            The map of reaction types to controllers.
     * @param reactionsByAnnotation
     *            The reaction annotations to controllers.
     */
    ReactStatement(Connector connector,
            IlkAssociation<Class<?>> reactionsByType,
            Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation) {
        this.connector = connector;
        this.reactionsByType = reactionsByType;
        this.reactionsByAnnotation = reactionsByAnnotation;
    }

    /**
     * Specify a type that will trigger a reaction.
     * 
     * @param ilk
     *            The trigger.
     * @return A react with clause to specify the reaction controllers.
     */
    public <T> ReactToTypeClause to(Ilk<T> ilk) {
       return new ReactToTypeClause(connector, reactionsByType, ilk.key);
    }
    
    /**
     * Specify a type that will trigger a reaction.
     * 
     * @param trigger
     *            The trigger.
  @return The react to type clause to specify the reaction controller.
     */
    public <T> ReactToTypeClause to(Class<T> trigger) {
       return to(new Ilk<T>(trigger));
    }

    /**
     * Specify an annotation that will trigger a reaction. The annotation must
     * be annotated with <code>Qualifier</code>. The reagent for the reaction
     * will be an <code>Object</code> qualified with the given qualifier.
     * 
     * @param qualifier
     *            The qualifier annotation.
     * @return A react to annotation clause to specify the reaction controller.
     */
    public ReactToAnnotationClause toQualifier(Class<? extends Annotation> qualifier) {
        return new ReactToAnnotationClause(connector, reactionsByAnnotation, qualifier);
    }
}
