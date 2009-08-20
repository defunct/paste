package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

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
public class OrConnector<T> {
    /** The parent element to return when the path statement is terminated. */
    private final T parent;

    /** A map of controller classes to globs that match them. */
    private final Map<Class<?>, Glob> controllerToGlob;

    /**
     * A list of globs to sets of rule mappings the further test to see if the
     * controller is applicable based on additional request parameters.
     */
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;

    /**
     * The list of parent glob compilers, one or each alternate path specified
     * by an or clause.
     */
    private final List<GlobCompiler> compilers;

    /** The rules to apply to a request after a path matches. */
    private final RuleMapBuilder<Pair<Integer, Class<?>>> rules;
    
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
    public OrConnector(
            T connector,
            Map<Class<?>, Glob> controllerToGlob,
            List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections,
            List<GlobCompiler> compilers,
            RuleMapBuilder<Pair<Integer, Class<?>>> rules,
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
    public PathConnector<T> path(String path) {
        List<String> orPatterns = new ArrayList<String>(patterns);
        orPatterns.add(path);
        return new PathConnector<T>(parent, controllerToGlob, connections, compilers, rules, orPatterns);
    }
}
