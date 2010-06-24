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
    
    // TODO Document.
    private final IlkAssociation<Class<?>> interceptors;
    
    // TODO Document.
    InterceptStatement(Connector connector, IlkAssociation<Class<?>> interceptors) {
        this.connector = connector;
        this.interceptors = interceptors;
    }
    
    // TODO Document.
    public <T> InterceptWithStatement assignableTo(Class<T> type) {
        return new InterceptWithStatement(connector, interceptors, null, new Ilk<T>(type).key);
    }

    // TODO Document.
    public InterceptWithStatement anntotatedWith(Class<? extends Annotation> annotation) {
        return new InterceptWithStatement(connector, interceptors, annotation, null);
    }
}
