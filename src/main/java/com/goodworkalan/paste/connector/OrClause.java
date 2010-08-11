package com.goodworkalan.paste.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.dovetail.PathCompiler;
import com.goodworkalan.paste.cassette.BindKey;
import com.goodworkalan.paste.cassette.Connection;
import com.goodworkalan.paste.cassette.ConnectionSet;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * An or clause in the path statement used to specify multiple paths for a path
 * to rules to controller binding.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public class OrClause<T> {
    /** The parent element to return when the path statement is terminated. */
    private final T parent;

    /** A map of controller classes to globs that match them. */
    private final Map<Class<?>, Path> controllerToGlob;

    /**
     * A list of globs to sets of rule mappings the further test to see if the
     * controller is applicable based on additional request parameters.
     */
    private final ConnectionSet<List<Connection>> connections;

    /**
     * The list of parent glob compilers, one or each alternate path specified
     * by an or clause.
     */
    private final List<PathCompiler> compilers;

    /** The rules to apply to a request after a path matches. */
    private final RuleMapBuilder<BindKey, Class<?>> rules;
    
    /**
     * The list of paths to compile, multiple paths can be specified using an or
     * clause. private final List<String> pattern;
     */ 
    private final List<String> patterns;

    /**
     * Construct an or clause.
     * 
     * @param connector
     *            The parent element to return when the path statement is
     *            terminated.
     * @param controllerToGlob
     *            A map of controller classes to globs that match them.
     * @param connections
     *            A list of globs to sets of rule mappings the further test to
     *            see if the controller is applicable based on additional
     *            request parameters.
     * @param compilers
     *            The list of parent glob compilers, one or each alternate path
     *            specified by an or clause.
     * @param rules
     *            The rules to apply to a request after a path matches.
     * @param patterns
     *            The list of paths to compile, multiple paths can be specified
     *            using an or clause. private final List<String> pattern;
     */
    OrClause(T connector,
             Map<Class<?>, Path> controllerToGlob,
             ConnectionSet<List<Connection>> connections,
             List<PathCompiler> compilers,
             RuleMapBuilder<BindKey, Class<?>> rules,
             List<String> patterns) {
        this.parent = connector;
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
        this.compilers = compilers;
        this.rules = rules;
        this.patterns = patterns;
    }

    /**
     * Specify an additional path to match for the path to controller binding.
     * 
     * @param path An additional path to match.
     * @return A path element to continue specifying path properties.
     */
    public PathStatement<T> path(String path) {
        List<String> orPatterns = new ArrayList<String>(patterns);
        orPatterns.add(path);
        return new PathStatement<T>(parent, controllerToGlob, connections, compilers, rules, orPatterns);
    }
}
