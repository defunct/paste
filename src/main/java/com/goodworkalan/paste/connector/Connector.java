package com.goodworkalan.paste.connector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.servlet.Cassette;
import com.mallardsoft.tuple.Pair;

/**
 * Used by {@link Router} instances to define path to controller mappings and
 * controller to renderer mappings.
 * 
 * @author Alan Gutierrez
 */
public class Connector {
    private final Cassette cassette;
    
    /**
     * Create a default connector.
     */
    public Connector(Cassette cassette) {
        cassette.reactions = new HashMap<Class<?>, List<Class<?>>>();
        cassette.controllerToGlob = new HashMap<Class<?>, Glob>();
        cassette.connections = new ArrayList<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>>();
        cassette.viewRules = new RuleMapBuilder<Pair<Integer, List<InjectorBuilder>>>();
        cassette.interceptors = new IlkAssociation<Class<?>>(true);
        this.cassette = cassette;
    }

    public ReactStatement react() {
        return new ReactStatement(this, cassette.reactions);
    }
    
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
        List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>> group = new ArrayList<Pair<List<Glob>,RuleMapBuilder<Pair<Integer,Class<?>>>>>();
        cassette.connections.add(group);
        return new ConnectStatement(this, cassette.controllerToGlob, group);
    }

    /**
     * Return an element in the domain-specific language that will map
     * controllers and exceptions to renderers, based on their type and on
     * additional request properties.
     * 
     * @return A domain-specific language used to define the renderer.
     */
    public RenderStatement render() {
        return new RenderStatement(this, cassette.viewRules);
    }
}
