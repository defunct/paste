package com.goodworkalan.paste.paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.qualifiers.Controller;

public class FormatTest {
    protected Class<?>[] args(Class<?>... formatArguments) {
        return formatArguments;
    }
    
    protected Injector getControllerInjector() {
        InjectorBuilder newInjector = new InjectorBuilder();
        newInjector.module(new InjectorBuilder() {
            protected void build() {
                instance(new Object(), ilk(Object.class), Controller.class);
            }
        });
        return newInjector.newInjector();
    }
    
    protected Injector getPathInjector(String path) {
        return getPathInjector(path, null);
    }

    
    protected Injector getPathInjector(String path, final String welcomeFile) {
        final Criteria criteria = mock(Criteria.class);
        when(criteria.getPath()).thenReturn(path);
        InjectorBuilder newInjector = new InjectorBuilder();
        newInjector.module(new InjectorBuilder() {
            protected void build() {
                instance(criteria, ilk(Criteria.class), null);
                if (welcomeFile != null) {
                    instance("index.html", ilk(String.class), WelcomeFile.class);
                }
            }
        });
        return newInjector.newInjector();
    }
}
