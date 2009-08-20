package com.goodworkalan.paste.paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Criteria;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class FormatTest {
    protected Class<?>[] args(Class<?>... formatArguments) {
        return formatArguments;
    }
    
    protected Injector getControllerInjector() {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Object.class).annotatedWith(Controller.class).toInstance(
                        new Object());
            }
        });
    }
    
    protected Injector getPathInjector(String path) {
        return getPathInjector(path, null);
    }

    
    protected Injector getPathInjector(String path, final String welcomeFile) {
        final Criteria criteria = mock(Criteria.class);
        when(criteria.getPath()).thenReturn(path);
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Criteria.class).toInstance(criteria);
                if (welcomeFile != null) {
                    bind(String.class).annotatedWith(WelcomeFile.class).toInstance("index.html");
                }
            }
        });
    }
}
