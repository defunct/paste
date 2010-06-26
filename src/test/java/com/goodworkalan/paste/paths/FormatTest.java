package com.goodworkalan.paste.paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.qualifiers.Controller;

/**
 * A base class with common methods to test format arguments.
 *
 * @author Alan Gutierrez
 */
public class FormatTest {
    /**
     * Construct an injector with an object bound to <code>Object</code> and
     * qualified by <code>Controller</code>.
     * 
     * @return An injector for testing injecting the controller.
     */
    protected Injector getControllerInjector() {
        InjectorBuilder newInjector = new InjectorBuilder();
        newInjector.module(new InjectorBuilder() {
            protected void build() {
                instance(new Object(), ilk(Object.class), Controller.class);
            }
        });
        return newInjector.newInjector();
    }
    
    /**
     * Create an injector for testing format arguments that operate on the
     * request path.
     * 
     * @param path
     *            The test path.
     * @return An injector for testing request path based format arguments.
     */
    protected Injector getPathInjector(String path) {
        return getPathInjector(path, null);
    }

    /**
     * Create an injector for testing format arguments that operate on the
     * request path that also require a welcome file.
     * 
     * @param path
     *            The test path.
     * @param welcomeFile
     *            The test welcome file.
     * @return An injector for testing request path based format arguments.
     */
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
