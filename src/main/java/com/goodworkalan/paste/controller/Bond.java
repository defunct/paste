package com.goodworkalan.paste.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The binding of the URL pattern to the URL.
 *
 * @author Alan Gutierrez
 */
public class Bond {
    /** The controller class. */
    public final Class<?> controllerClass;
    
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
    public Bond(String path, String suffix, boolean includesSuffix) {
        this.controllerClass = null;
        this.parameters = new HashMap<String, String>();
        this.path = path;
        this.suffix = suffix;
        this.includesSuffix = includesSuffix;
    }

    /**
     * Create a binding.
     * 
     * @param bond
     *            The initial bond.
     * @param controllerClass
     *            The controller class.
     * @param parameters
     *            The parameters extracted by the URL match.
     */
    public Bond(Bond bond, Class<?> controllerClass, Map<String, String> parameters) {
        this.controllerClass = controllerClass;
        this.parameters = Collections.<String, String>unmodifiableMap(parameters);
        this.path = bond.path;
        this.suffix = bond.suffix;
        this.includesSuffix = bond.includesSuffix;
    }
    
    public Bond(Bond bond, Class<?> controllerClass) {
        this(bond, controllerClass, bond.parameters);
    }
}
