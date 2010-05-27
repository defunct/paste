package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;

/**
 * The with clause in for an intercept statement in the builder language.
 * 
 * @author Alan Gutierrez
 */
public class InterceptWithStatement {
    /** The connector. */
    private final Connector connector;

    /** The map of types to interceptors. */
    private final IlkAssociation<Class<?>> interceptors;

    /**
     * The annotation to associate with the interceptor class, or null if the
     * mapping is to a type.
     */
    private final Class<? extends Annotation> annotation;

    /**
     * The annotation to associate with the key class, or null if the
     * mapping is to an annotation.
     */
    private final Ilk.Key key;

    /**
     * Create an intercept with clause.
     * 
     * @param connector
     *            The connector.
     * @param interceptors
     *            The map of types to interceptors.
     * @param annotation
     *            The annotation to associate with the interceptor class, or
     *            null if the mapping is to a type.
     * @param key
     *            The annotation to associate with the key class, or null if the
     *            mapping is to an annotation.
     */
    InterceptWithStatement(Connector connector, IlkAssociation<Class<?>> interceptors, Class<? extends Annotation> annotation, Ilk.Key key) {
        this.connector = connector;
        this.interceptors = interceptors;
        this.annotation = annotation;
        this.key = key;
    }

    /**
     * Assign the interceptor for the intercept statement.
     * 
     * @param interceptor
     *            The interceptor class.
     * @return The connector to conintue building the application.
     */
    public Connector with(Class<?> interceptor) {
        if (key != null) {
            interceptors.assignable(key, interceptor);
        } else {
            interceptors.annotated(annotation, interceptor);
        }
        return connector;
    }
}
