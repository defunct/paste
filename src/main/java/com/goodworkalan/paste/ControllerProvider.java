package com.goodworkalan.paste;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.ilk.inject.Injector;

public class ControllerProvider implements Provider<Object> {
    private final Injector injector;
    
    private final Class<?> controllerClass;
    
    @Inject
    public ControllerProvider(Injector injector, Class<?> controllerClass) {
        this.injector = injector;
        this.controllerClass = controllerClass;
    }
    
    public Object get() {
        return injector.create(controllerClass, null);
    }
}
