package com.goodworkalan.paste;

import java.util.Map;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.goodworkalan.dovetail.Glob;
import com.mallardsoft.tuple.Pair;

// TODO Document.
public class RuleConnector<T> {
    /**
     * The glob to return when the glob for the controller for this when clause
     * is requested.
     */
    private final Glob glob;

    private final Map<Class<?>, Glob> controllerToGlob;

    /**
     * A terminal language element that will return a language element that can
     * either specify more rule sets or termiante the path statement.
     */
    private final NextRuleConnector<T> nextRuleConnector;

    /** The builder of map of rule sets to priority and controller class pairs. */
    protected final RuleMapBuilder<Pair<Integer, Class<?>>> rules;

    /** The rule set builder for this rule statement. */
    protected final RuleSetBuilder<Pair<Integer, Class<?>>> rule;

    /** The priority of the rule set to resolve ambiguities. */
    private int priority;

    /**
     * 
     * @param nextRuleConnector A terminal language element that will return a language element
     *         that can either specify more rule sets or termiante the path
     *         statement.
     * @param glob
     *            The glob to return when the glob for the controller for this
     *            when clause is requested.
     * @param controllerToGlob
     * @param rules
     */
    public RuleConnector(PathStatement<T> nextRuleConnector, Glob glob,
            Map<Class<?>, Glob> controllerToGlob,
            RuleMapBuilder<Pair<Integer, Class<?>>> rules) {
        this.glob = glob;
        this.controllerToGlob = controllerToGlob;
        this.nextRuleConnector = nextRuleConnector;
        this.rules = rules;
        this.rule = rules.rule();
    }

    /**
     * Specify the request methods that the rule matches.
     * 
     * @param methods
     *            The request methods.
     * @return This rule element to continue specifying rules.
     */
    public RuleConnector<T> method(String... methods) {
        for (String method : methods) {
            rule.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }

    /**
     * Set the priority of this set of rules.
     * 
     * @param priority
     *            The priority.
     * @return This rule element to continue specifying rules.
     */
    public RuleConnector<T> priority(int priority) {
        this.priority = priority;
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
    public Ending<NextRuleConnector<T>> to(Class<?> controller) {
        if (!controllerToGlob.containsKey(controller)) {
            controllerToGlob.put(controller, glob);
        }
        rule.put(new Pair<Integer, Class<?>>(priority, controller));
        return new Ending<NextRuleConnector<T>>(nextRuleConnector);
    }
}
