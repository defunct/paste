package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;

/**
 * A builder for specifying intercepting controllers that bind to controller
 * types and are invoked prior to the invocation bound controller to intercept
 * the request processing based on controller type.
 * 
 * @author Alan Gutierrez
 */
public class InterceptStatement {
    /** The connector to return when the statement terminates. */
    private final Connector connector;
    
    /** The super type token to interceptor associations. */
    private final IlkAssociation<Class<?>> interceptors;
    
    /**
     * Create a new intercept statement.
     * 
     * @param connector
     *            The connector to return when the statement terminates.
     * @param interceptors
     *            The super type token to interceptor associations.
     */
    InterceptStatement(Connector connector, IlkAssociation<Class<?>> interceptors) {
        this.connector = connector;
        this.interceptors = interceptors;
    }

    /**
     * Associate an interceptor with controllers assignable to the given type.
     * 
     * @param <T>
     *            The controller super type or interface.
     * @param type
     *            The controller super class or interface.
     * @return A builder to specify the intercptor.
     */
    public <T> InterceptWithStatement assignableTo(Class<T> type) {
        return new InterceptWithStatement(connector, interceptors, null, new Ilk<T>(type).key);
    }

    /**
     * Associate an interceptor with controllers annotated with the given
     * annotation.
     * 
     * @param annotation
     *            The annotation.
     * @return A builder to specify the intercptor.
     */
    public InterceptWithStatement anntotatedWith(Class<? extends Annotation> annotation) {
        return new InterceptWithStatement(connector, interceptors, annotation, null);
    }
}
