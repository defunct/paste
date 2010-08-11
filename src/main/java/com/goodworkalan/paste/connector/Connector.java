package com.goodworkalan.paste.connector;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.cassette.BindKey;
import com.goodworkalan.paste.cassette.Cassette;
import com.goodworkalan.paste.cassette.Connection;
import com.goodworkalan.paste.cassette.ConnectionSet;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * Used by {@link Router} instances to define path to controller mappings and
 * controller to renderer mappings.
 * 
 * @author Alan Gutierrez
 */
public class Connector {
    /** The connection data structure to populate. */
    private final Cassette cassette;

    /**
     * Create a default connector.
     * 
     * @param cassette
     *            The connection data structure to populate.
     */
    public Connector(Cassette cassette) {
        cassette.reactionsByType = new IlkAssociation<Class<?>>(true);
        cassette.reactionsByAnnotation = new HashMap<Class<? extends Annotation>, List<Class<?>>>();
        cassette.routes = new HashMap<Class<?>, Path>();
        cassette.connections = new ArrayList<ConnectionSet<List<Connection>>>();
        cassette.renderers = new RuleMapBuilder<BindKey, List<InjectorBuilder>>();
        cassette.interceptors = new IlkAssociation<Class<?>>(true);
        this.cassette = cassette;
    }

    /**
     * Syntactical sugar for importing existing routers, applies the router to
     * this connector and returns this connector.
     * 
     * @param router
     *            The router to apply to this connector.
     * @return This connector to continue specifying routes.
     */
    public Connector module(Router router) {
        router.connect(this);
        return this;
    }

    /**
     * Specify a controller for an event that is not directly associated with an
     * HTTP request. This is used by timers to implement delayed jobs or
     * background tasks. Reaction controllers are built using dependency
     * injection, so that application scoped resources are available during the
     * reaction.
     * 
     * @return A react statement to specify an event reactor.
     */
    public ReactStatement react() {
        return new ReactStatement(this, cassette.reactionsByType, cassette.reactionsByAnnotation);
    }

    /**
     * Specify intercepting controllers that bind to controller types and are
     * invoked prior to the invocation bound controller to intercept the request
     * processing based on controller type.
     * 
     * @return An intercept statement to specify controller interceptors.
     */
    public InterceptStatement intercept() {
        return new InterceptStatement(this, cassette.interceptors);
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
    public ConnectStatement connect() {
        ConnectionSet<List<Connection>> group = new ConnectionSet<List<Connection>>(new ArrayList<Connection>());
        cassette.connections.add(group);
        return new ConnectStatement(this, cassette.routes, group);
    }

    /**
     * Return an element in the domain-specific language that will map
     * controllers and exceptions to renderers, based on their type and on
     * additional request properties.
     * 
     * @return A domain-specific language used to define the renderer.
     */
    public RenderStatement render() {
        return new RenderStatement(this, cassette.renderers);
    }
    
    /** End a connection statement. */
    public void end() {
    }
}
