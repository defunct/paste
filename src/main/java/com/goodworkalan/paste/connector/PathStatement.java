package com.goodworkalan.paste.connector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.dovetail.PathCompiler;
import com.goodworkalan.paste.cassette.BindKey;
import com.goodworkalan.paste.cassette.Connection;
import com.goodworkalan.paste.cassette.ConnectionSet;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * A path element in the domain-specific language for
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public class PathStatement<T> implements SubPathClause<T> {
    /** The parent element to return when the path statement is terminated. */
    private final T connector;

    /** A map of controller classes to globs that match them. */
    private final Map<Class<?>, Path> controllerToPath;

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

    /**
     * The list of path expressions for this path element, one for each path
     * specified.
     */
    private final List<Path> paths;

    /** The rules to apply to a request after a path matches. */
    private final RuleMapBuilder<BindKey, Class<?>> rules;

    /**
     * The list of paths to compile, multiple paths can be specified using an or
     * clause. private final List<String> pattern;
     */ 
    private final List<String> patterns;

    /** Flag indicating that the path or paths have been compiled. */
    private boolean compiled;

    /**
     * Construct a path element in the domain-specific URL binding language.
     * 
     * @param connector
     *            The parent element to return when the path statement is
     *            terminated.
     * @param controllerToPath
     *            A map of controller classes to path that match them.
     * @param connections
     *            A list of path expressions to sets of rule mappings the
     *            further test to see if the controller is applicable based on
     *            additional request parameters.
     * @param compilers
     *            The list of parent path compilers, one or each alternate path
     *            specified by an or clause.
     * @param rules
     *            The map of rules to apply to a request after a path matches.
     * @param patterns
     *            The list of paths for this binding.
     */
    PathStatement(
            T connector,
            Map<Class<?>, Path> controllerToPath,
            ConnectionSet<List<Connection>> connections,
            List<PathCompiler> compilers,
            RuleMapBuilder<BindKey, Class<?>> rules,
            List<String> patterns) {
        this.controllerToPath = controllerToPath;
        this.connector = connector;
        this.connections = connections;
        this.compilers = compilers;
        this.paths = new ArrayList<Path>();
        this.rules = rules;
        this.patterns = patterns;
    }

    /**
     * Specify an additional path for this path statement to match.
     * 
     * @return An or language element to match an additional path.
     */
    public OrClause<T> or() {
        return new OrClause<T>(connector, controllerToPath, connections, compilers, rules, patterns);
    }

    /**
     * Compiles the paths into Dovetail globs if they have not already been
     * compiled.
     */
    private void compile() {
        if (!compiled) {
            for (PathCompiler compiler : compilers) {
                for (String pattern : patterns) {
                    paths.add(compiler.compile(pattern));
                }
            }
            compiled = true;
        }
    }

    /**
     * Begin a sub-path statement whose path is a sub-path of all the paths
     * specified by this path statement.
     * 
     * @return A path language element to specify a sub-path.
     */
    public PathStatement<SubPathClause<T>> path(String path) {
        compile();
        List<PathCompiler> subCompilers = new ArrayList<PathCompiler>();
        for (Path glob : paths) {
            subCompilers.add(new PathCompiler(glob));
        }
        List<Path> globs = new ArrayList<Path>();
        RuleMapBuilder<BindKey, Class<?>> rules = new RuleMapBuilder<BindKey, Class<?>>();
        connections.association.add(new Connection(globs, rules));
        return new PathStatement<SubPathClause<T>>(this, controllerToPath, connections, subCompilers, rules, Collections.singletonList(path));
    }
    
    public PathStatement<T> includeSuffix() {
        return this;
    }

    /**
     * Begin a rule statement to specify rules based on on request properties
     * other than the path.
     * 
     * @return An element that creates rules based on request properties other
     *         than the path.
     */
    public WhenStatement<T> when() {
        compile();
        connections.association.add(new Connection(paths, rules));
        return new WhenStatement<T>(this, paths.get(0), controllerToPath, rules);
    }

    /**
     * Bind the current path to the given controller, returning a terminal
     * laguage element.
     * 
     * @param controller
     *            The controller bound to this path.
     */
    public End<T> to(Class<?> controller) {
        compile();
        rules.rule().put(controller);
        controllerToPath.put(controller, paths.get(0));
        connections.association.add(new Connection(paths, rules));
        return new End<T>(connector);
    }

    /**
     * End this path statement returning the parent language element.
     * 
     * @return The parent language element.
     */
    public T end() {
        return connector;
    }
}
