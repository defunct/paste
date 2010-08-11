package com.goodworkalan.paste.cassette;

import java.util.List;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * Used within a <code>List</code>, this structure associates a set of Dovetail
 * <code>Path</code> instances with a <code>RuleMapBuilder</code> that
 * associates request properties to controllers.
 * 
 * @author Alan Gutierrez
 */
public class Connection {
    /** The paths that match this connection. */
    public List<Path> paths;

    /**
     * The set of rules to further qualify a connection based on request
     * properties.
     */
    public RuleMapBuilder<BindKey, Class<?>> rules;

    /**
     * Create a connection with the given set of paths and the given rule map.
     * 
     * @param paths
     *            The paths that match this connection.
     * @param rules
     *            The set of rules to further qualify a connection based on
     *            request properties.
     */
    public Connection(List<Path> paths, RuleMapBuilder<BindKey, Class<?>> rules) {
        this.paths = paths;
        this.rules = rules;
    }
}