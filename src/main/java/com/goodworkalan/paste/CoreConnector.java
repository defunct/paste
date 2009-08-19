package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

/**
 * Default implementation of the {@link Connector} interface that exposes
 * methods to retrieve the bindings defined through the connector.
 * 
 * @author Alan Gutierrez
 */
public class CoreConnector implements Connector
{
    /** A map of controller classes to the globs that define their URL bindings. */
    private Map<Class<?>, Glob> controllerToGlob;
    
    /**
     * A list of connection groups, a connection group consisting of URL
     * bindings. URL bindings consisting of a list of globs paired with rule map
     * of tests to further winnow the match based on request parameters.
     */
    private final List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections;
    
    /** A rule map to match a controller or exception to a renderer. */
    private final RuleMapBuilder<Pair<Integer, RenderModule>> viewRules;
    
    /**
     * Create a default connector.
     */
    public CoreConnector() {
        this.controllerToGlob = new HashMap<Class<?>, Glob>();
        this.connections = new ArrayList<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>>();
        this.viewRules = new RuleMapBuilder<Pair<Integer, RenderModule>>();
    }

    /**
     * Create a new group of connections that will map paths to controller
     * classes. When processing a request, each group of connections is
     * evaluated in the order in which they were defined. Each group of
     * connections has the opportunity to match the path and apply a controller
     * to the request. If the controller intercepts the request, either by
     * explicitly calling intercept, throwing an exception, or writing output,
     * connection group evaluation processing, no further connection groups are
     * evaluated, and view evaluation begins.
     * 
     * @return A domain-specific language element used to define a group
     */
    public ConnectionGroup connect() {
        List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> group = new ArrayList<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>();
        connections.add(group);
        return new ConnectionGroup(controllerToGlob, group);
    }

    /**
     * Return an element in the domain-specific language that will map
     * controllers and exceptions to renderers, based on their type and on
     * additional request properties.
     * 
     * @return A domain-specific language used to define the renderer.
     */
    public ViewConnector view() {
        return new ViewConnector(null, viewRules, Collections.singletonList(viewRules.rule()));
    }

    /**
     * Create a routes object that will return the controller mapped to a given
     * URL.
     * 
     * @return A controller lookup.
     */
    public Routes getRoutes() {
        return new Routes(controllerToGlob);
    }

    /**
     * Builds a list of connection groups, where a connection group is a glob
     * tree that maps to a rule map of tests to further winnow the match based
     * on request parameters. This method builds the glob trees from their
     * intermediate state, which is the list of globs to add to the glob tree. 
     * 
     * @return The list of connection groups.
     */
    public List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> getBindingTrees() {
        List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> trees = new ArrayList<GlobTree<RuleMap<Pair<Integer, Class<?>>>>>();
        for (List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> listOfControllerPathMappings : connections) {
            GlobTree<RuleMap<Pair<Integer, Class<?>>>> tree = new GlobTree<RuleMap<Pair<Integer, Class<?>>>>();
            for (Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>> mapping : listOfControllerPathMappings) {
                RuleMap<Pair<Integer, Class<?>>> rules = Tuple.get2(mapping).newRuleMap();
                for (Glob glob : Tuple.get1(mapping)) {
                    tree.add(glob, rules);
                }
            }
            trees.add(tree);
        }
        return trees;
    }

    /**
     * Create the rule map to match a controller or exception to a renderer.
     * 
     * @return The rule map to match a controller or exception to a renderer.
     */
    public RuleMap<Pair<Integer, RenderModule>> getViewRules() {
        return viewRules.newRuleMap();
    }
}
