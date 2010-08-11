package com.goodworkalan.paste.connector;

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
 * A builder for a group of connections in the domain-specific controller
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
    private final Map<Class<?>, Path> controllerToGlob;
    
    /**
     * A list of globs to sets of rule mappings the further test to see if the
     * controller is applicable based on additional request parameters.
     */
    private final ConnectionSet<List<Connection>> connections;

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
    ConnectStatement(Connector end, Map<Class<?>, Path> controllerToGlob, ConnectionSet<List<Connection>> connections)  {
        this.end = end;
        this.controllerToGlob = controllerToGlob;
        this.connections = connections;
    }

    /**
     * Include the file suffix when comparing the path against the set of path
     * matching patterns defined by this connect statement.
     * 
     * @return This connect statement to continue defining connection
     *         properties.
     */
    public ConnectStatement includeSuffix() {
        connections.includeSuffix = true;
        return this;
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
        return new PathStatement<ConnectStatement>(this, controllerToGlob, connections, Collections.singletonList(new PathCompiler()), new RuleMapBuilder<BindKey, Class<?>>(), Collections.singletonList(path));
    }

    /**
     * Terminate the connect statement and return the connector.
     * 
     * @return The connector to continue building the application.
     */
    public Connector end() {
        return end;
    }
}
