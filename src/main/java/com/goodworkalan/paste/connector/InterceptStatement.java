package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.association.IlkAssociation;

public class InterceptStatement {
    private final Connector connector;
    
    private final IlkAssociation<Class<?>> interceptors;
    
    InterceptStatement(Connector connector, IlkAssociation<Class<?>> interceptors) {
        this.connector = connector;
        this.interceptors = interceptors;
    }
    
    public <T> InterceptWithStatement assignableTo(Class<T> type) {
        return new InterceptWithStatement(connector, interceptors, null, new Ilk<T>(type).key);
    }

    public InterceptWithStatement anntotatedWith(Class<? extends Annotation> annotation) {
        return new InterceptWithStatement(connector, interceptors, annotation, null);
    }
}
