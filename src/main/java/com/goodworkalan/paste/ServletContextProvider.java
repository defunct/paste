package com.goodworkalan.paste;

import java.lang.ref.WeakReference;

import javax.servlet.ServletContext;

import com.google.inject.Provider;

/**
 * Provides the servlet context used to create the paste filter.
 * 
 * @author Alan Gutierrez
 */
public class ServletContextProvider implements Provider<ServletContext> {
    /** Weak reference to the servlet context for the paste filter. */
    private final WeakReference<ServletContext> servletContext;

    /**
     * Create a servlet context provider that provides the given servlet
     * context.
     * 
     * @param servletContext
     *            The servlet context.
     */
    public ServletContextProvider(ServletContext servletContext) {
        this.servletContext = new WeakReference<ServletContext>(servletContext);
    }

    /**
     * Get the servlet context.
     * 
     * @return The servlet context.
     */
    public ServletContext get() {
        return servletContext.get();
    }
}
