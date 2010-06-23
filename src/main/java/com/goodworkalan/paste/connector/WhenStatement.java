package com.goodworkalan.paste.connector;

import java.util.Map;

import com.goodworkalan.winnow.Equals;
import com.goodworkalan.winnow.RuleMapBuilder;
import com.goodworkalan.winnow.RuleSetBuilder;
import com.goodworkalan.dovetail.Path;
import com.goodworkalan.paste.servlet.BindKey;
import com.goodworkalan.paste.servlet.Cassette;

/**
 * A statement that specifies the rules to apply after a path has matched to
 * choose a controller based on additional request properties.
 * 
 * @author Alan Gutierrez
 * 
 * @param <T>
 *            The type of parent element to return when the statement is
 *            terminated.
 */
public class WhenStatement<T> {
    /**
     * A terminal language element that will return a language element that can
     * either specify more rule sets or termiante the path statement.
     */
    private final WhenClause<T> when;

    /**
     * The glob to return when the glob for the controller for this when clause
     * is requested.
     */
    private final Path glob;

    /** A map of controller classes to globs that match them. */
    private final Map<Class<?>, Path> controllerToGlob;

    /** The rule set builder for this rule statement. */
    private final RuleSetBuilder<Cassette.ControllerCandidate> rule;

    /** The priority of the rule set to resolve ambiguities. */
    private int priority;

    /**
     * 
     * @param when
     *            A terminal language element that will return a language
     *            element that can either specify more rule sets or termiante
     *            the path statement.
     * @param glob
     *            The glob to return when the glob for the controller for this
     *            when clause is requested in the routes reverse lookup.
     * @param controllerToGlob
     *            A map of controller classes to globs that match them.
     * @param rules
     *            The builder of map of rule sets to priority and controller
     *            class pairs.
     */
    WhenStatement(PathStatement<T> when, Path glob, Map<Class<?>, Path> controllerToGlob, RuleMapBuilder<Cassette.ControllerCandidate> rules) {
        this.when = when;
        this.glob = glob;
        this.controllerToGlob = controllerToGlob;
        this.rule = rules.rule();
    }

    /**
     * Specify the request methods that the rule matches.
     * 
     * @param methods
     *            The request methods.
     * @return This rule element to continue specifying rules.
     */
    public WhenStatement<T> method(String... methods) {
        for (String method : methods) {
            rule.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }

    /**
     * Set the priority of this set of rules.
     * <p>
     * FIXME Outgoing? Too bad it kind of overshadows the priorities
     * set by the PathAssociation, but still needed for Winnow.
     * 
     * @param priority
     *            The priority.
     * @return This rule element to continue specifying rules.
     */
    public WhenStatement<T> priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Specify the suffixes that this rule matches.
     * 
     * @param suffixes
     *            The suffixes.
     * @return This when statement to continue specifying rules.
     */
    public WhenStatement<T> suffix(String...suffixes) {
        for (String suffix : suffixes) {
            rule.check(BindKey.SUFFIX, new Equals(suffix));
        }
        return this;
    }

    /**
     * Set the controller class to use when this set of rules is matched.
     * 
     * @param controller
     *            The controller class.
     * @return A terminal language element that will return a language element
     *         that can either specify more rule sets or termiante the path
     *         statement.
     */
    public End<WhenClause<T>> to(Class<?> controller) {
        // Record that first glob as the path that is returned during reverse lookup.
        if (!controllerToGlob.containsKey(controller)) {
            controllerToGlob.put(controller, glob);
        }
        rule.put(new Cassette.ControllerCandidate(priority, controller));
        return new End<WhenClause<T>>(when);
    }
}
