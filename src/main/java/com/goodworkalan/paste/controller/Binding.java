package com.goodworkalan.paste.controller;

import java.util.Map;

/**
 * The binding of the URL pattern to the URL.
 *
 * @author Alan Gutierrez
 */
public class Binding {
    /** The parameters extracted from the URL. */
    public final Map<String, String> parameters;
    
    /** The URL path. */
    public final String path;
    
    /** The URL suffix. */
    public final String suffix;
    
    /** Whether the suffix is included in the path. */
    public final boolean includesSuffix;

    /**
     * Create a binding.
     * 
     * @param parameters
     *            The parameters extracted from the URL.
     * @param path
     *            The URL path.
     * @param suffix
     *            The URL suffix.
     * @param includesSuffix
     *            Whether the suffix is included in the path.
     */
    public Binding(Map<String, String> parameters, String path, String suffix, boolean includesSuffix) {
        this.parameters = parameters;
        this.path = path;
        this.suffix = suffix;
        this.includesSuffix = includesSuffix;
    }
}
