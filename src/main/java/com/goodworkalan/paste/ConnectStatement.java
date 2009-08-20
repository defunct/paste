package com.goodworkalan.paste;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobCompiler;
import com.mallardsoft.tuple.Pair;

/**
 * An element for a group of connections in the domain-specific object to URL
 * binding language. An router can specify multiple connection groups. Each
 * group is tested against the request path in turn, selecting a single
 * controller for each group, until all groups have been applied or one of the
 * controllers intercepts the filter chain by sending a response.
 * 
 * @author Alan Gutierrez
 */
public class ConnectStatement {
    /** The connector to return when the statement terminates. */
    private final Connector end;
    
    /** A map of controller classes to globs that match them. */
    private final Map<Class<?>, Glob> controllerToGlob;
    
    /**
     * A list of globs to sets of rule mappings the further test to see if the
     * controller is applicable based on additional request parameters.
     */
    private final List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections;

    /**
     * Create a connector group that will populate the given data structures
     * which are held by the connector.
     * 
     * @param end
     *            The connector to return when the statement terminates.
     * 
     * @param controllerToGlob
     *            A map of controller classes to globs that match them.
     * @param connections
     *            A list of globs to sets of rule mappings the further test to
     *            see if the controller is applicable based on additional
     *            request parameters.
     */
    public ConnectStatement(Connector end, Map<Class<?>, Glob> controllerToGlob, List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> connections)  {
        this.end = end;
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
    }

    /**
     * Begin a path statement in the domain-specific object to URL binding
     * language.
     * 
     * @param path
     *            The path to bind.
     * @return A path specification element to define the path.
     */
    public PathStatement<ConnectStatement> path(String path) {
        return new PathStatement<ConnectStatement>(this, controllerToGlob, connections, Collections.singletonList(new GlobCompiler()), new RuleMapBuilder<Pair<Integer,Class<?>>>(), Collections.singletonList(path));
    }

    /**
     * Terminate the connect statement and return teh connector.
     * 
     * @return The connector to continue to specify connections and renderers.
     */
    public Connector end() {
        return end;
    }
}
