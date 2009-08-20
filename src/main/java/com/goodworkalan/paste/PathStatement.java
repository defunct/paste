package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.goodworkalan.dovetail.MatchTest;
import com.mallardsoft.tuple.Pair;

/**
 * A path element in the domain-specific language for
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public class PathStatement<T> implements FilterClause<T> {
    /** The parent element to return when the path statement is terminated. */
    private final T connector;

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

    /** The list of globs for this path element, one for each path specified. */
    private final List<Glob> globs;

    /** The rules to apply to a request after a path matches. */
    private final RuleMapBuilder<Pair<Integer, Class<?>>> rules;

    /**
     * The list of paths to compile, multiple paths can be specified using an or
     * clause. private final List<String> pattern;
     */ 
    private final List<String> patterns;

    /** The priority of this path or paths relative to other paths that match. */
    private int priority;

    /** Flag indicating that the path or paths have been compiled. */
    private boolean compiled;

    /**
     * Construct a path element in the domain-specific URL binding language.
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
     * @param globs
     *            The list of globs for this path element, one for each path
     *            specified.
     * @param rules
     *            The map of rules to apply to a request after a path matches.
     * @param patterns
     *            The list of paths for this binding.
     */
    public PathStatement(
            T connector,
            Map<Class<?>, Glob> controllerToGlob,
            List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections,
            List<GlobCompiler> compilers,
            RuleMapBuilder<Pair<Integer, Class<?>>> rules,
            List<String> patterns) {
        this.controllerToGlob = controllerToGlob;
        this.connector = connector;
        this.connections = connections;
        this.compilers = compilers;
        this.globs = new ArrayList<Glob>();
        this.rules = rules;
        this.patterns = patterns;
    }

    /**
     * Specify an additional path for this path statement to match.
     * 
     * @return An or language element to match an additional path.
     */
    public OrConnector<T> or() {
        return new OrConnector<T>(connector, controllerToGlob, connections, compilers, rules, patterns);
    }

    /**
     * Specify a Dovetail match filter instance to apply against any matched
     * paths.
     * 
     * @param matchTest
     *            A match filter instance.
     * @return A filter clause to continue to specify match filters or to
     *         specify rules.
     */
    public FilterClause<T> filtered(MatchTest matchTest) {
        for (GlobCompiler compiler : compilers) {
            compiler.test(matchTest);
        }
        return this;
    }

    /**
     * Specify a Dovetail match filter class to apply against any matched paths.
     * The match filter class will be constructed by the same Guice injector
     * used to construct controllers. The match filter can take advantage of the
     * servlet, request and filter scopes.
     * 
     * @param matchTestClass
     *            A match filter class.
     * @return A filter clause to continue to specify match filters or to
     *         specify rules.
     */
    public FilterClause<T> filter(Class<? extends MatchTest> matchTestClass) {
        for (GlobCompiler compiler : compilers) {
            compiler.test(matchTestClass);
        }
        return this;
    }

    /**
     * Compiles the paths into Dovetail globs if they have not already been
     * compiled.
     */
    private void compile() {
        if (!compiled) {
            for (GlobCompiler compiler : compilers) {
                for (String pattern : patterns) {
                    globs.add(compiler.compile(pattern));
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
        List<GlobCompiler> subCompilers = new ArrayList<GlobCompiler>();
        for (Glob glob : globs) {
            subCompilers.add(new GlobCompiler(glob));
        }
        List<Glob> globs = new ArrayList<Glob>();
        RuleMapBuilder<Pair<Integer, Class<?>>> rules = new RuleMapBuilder<Pair<Integer, Class<?>>>();
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>(globs, rules));
        return new PathStatement<SubPathClause<T>>(this, controllerToGlob, connections, subCompilers, rules, Collections.singletonList(path));
    }

    /**
     * Assign a priority to current path. Followed by a <code>to</code>
     * connector assignment.
     * 
     * @param priority
     *            The priority for this path.
     * @return A connector that provides only a <code>to</code> method.
     */
    public ToConnector<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Begin a rule statement to specify rules based on on request properties
     * other than the path.
     * 
     * @return An element that creates rules based on request properties other
     *         than the path.
     */
    public RuleConnector<T> when() {
        compile();
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>(globs, rules));
        return new RuleConnector<T>(this, globs.get(0), controllerToGlob, rules);
    }

    /**
     * Bind the current path to the given controller, returning a terminal
     * laguage element.
     * 
     * @param controller
     *            The controller bound to this path.
     */
    public Ending<T> to(Class<?> controller) {
        compile();
        rules.rule().put(new Pair<Integer, Class<?>>(priority, controller));
        controllerToGlob.put(controller, globs.get(0));
        connections.add(new Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>(globs, rules));
        return new Ending<T>(connector);
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
