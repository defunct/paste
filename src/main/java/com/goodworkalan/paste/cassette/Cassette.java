package com.goodworkalan.paste.cassette;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dovetail.Path;
import com.goodworkalan.dovetail.PathAssociation;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.winnow.RuleMap;
import com.goodworkalan.winnow.RuleMapBuilder;

/**
 * This cassette is given to the {@link Controller} and acts as the interface
 * between the Servlet implementation package and the controller domain-specific
 * language package. The domain-specific builder language belongs in its own package,
 * but the domain-specific language is tightly bound to the 
 * <p>
 * It not very interesting for application developers, but there is no damage
 * you can do by instancinating it. This cassette pattern basically says, it is
 * not too pleasant to have everything in one package, and a domain-specific
 * language looks nicer in its own package, but a domain-specific langauge is
 * often tightly bound to the item it creates. The cassette contains the data
 * built in a very raw form, as maps and collections and other structural
 * classes. The calling package will create a cassette, give it to the
 * domain-specific language route, then give the language root to interpreters
 * that will build something with the language.
 * <p>
 * For added safety, the domain-specific language root will reset all of the
 * connections when it is created, to prevent people from complaining that when
 * they built this own connector, it gave them a very evil error. Here is yet
 * another place where immutability is disfavored.
 * <p>
 * This means that this particular exposure of implementation detail is about as
 * benign as can be. Yes it is an implementation detail that is exposed through
 * the public API, one that is subject to change, and not to be depended upon.
 * But the nice thing is that the data here is so useless, such a dead end, that
 * there is no good way to integrate this information into your application. You
 * cannot intercept the data as used by the Servlet filter. You're welcome to
 * create cassettes and run them through connector and inspect the output in
 * testing, but there is no use for it during runtime.
 * 
 * @author Alan Gutierrez
 */
public final class Cassette {
    /**
     * Associates a set of paths with with a rule map that associates request
     * properties to controllers.
     * 
     * @author Alan Gutierrez
     */
    public static final class Connection {
        /** The paths that match this connection. */
        public List<Path> paths;
        
        /** Whether to include the suffix when matching. */
        public boolean includeSuffix;
        
        /**
         * The set of rules to further qualify a connection based on request
         * properties.
         */
        public RuleMapBuilder<BindKey, Class<?>> rules;
        
        /**
         * Create a connection with the given set of paths and the given rule
         * map.
         * 
         * @param paths
         *            The paths that match this connection.
         * @param rules
         *            The set of rules to further qualify a connection based on
         *            request properties.
         */
        public Connection(List<Path> paths, RuleMapBuilder<BindKey, Class<?>> rules) {
            this.paths = paths;
            this.rules = rules;
        }
    }

    /** A map of controller classes to the globs that define their URL bindings. */
    public Map<Class<?>, Path> routes;

    /**
     * A list of connection groups, a connection group consisting of URL
     * bindings. URL bindings consisting of a list of globs paired with rule map
     * of tests to further winnow the match based on request parameters.
     */
    public List<List<Connection>> connections;
    
    /** A rule map to match a controller or exception to a renderer. */
    public RuleMapBuilder<BindKey, List<InjectorBuilder>> renderers;
    
    /** The map of reaction types to controllers. */
    public IlkAssociation<Class<?>> reactionsByType;

    /** The reaction annotations to controllers. */
    public Map<Class<? extends Annotation>, List<Class<?>>> reactionsByAnnotation;

    /** The type to interceptor association. */
    public IlkAssociation<Class<?>> interceptors;
    
    /**
     * Create a default connector.
     */
    public Cassette() {
    }

    /**
     * Get the map of controller classes to Dovetail <code>Path</code>
     * instances.
     * 
     * @return A controller lookup.
     */
    Map<Class<?>, Path> getRoutes() {
        return routes;
    }

    /**
     * Builds a list of connection groups, where a connection group is a
     * Dovetail path association tree that maps to a rule map of tests to
     * further winnow the match based on request parameters. This method builds
     * the path associations from their intermediate state, which is the list of
     * paths to add to the path association.
     * 
     * @return The list of connection groups.
     */
    List<PathAssociation<RuleMap<BindKey, Class<?>>>> getConnections() {
        List<PathAssociation<RuleMap<BindKey, Class<?>>>> trees = new ArrayList<PathAssociation<RuleMap<BindKey, Class<?>>>>();
        for (List<Connection> listOfControllerPathMappings : connections) {
            PathAssociation<RuleMap<BindKey, Class<?>>> tree = new PathAssociation<RuleMap<BindKey, Class<?>>>();
            for (Connection mapping : listOfControllerPathMappings) {
                RuleMap<BindKey, Class<?>> rules = mapping.rules.newRuleMap();
                for (Path glob : mapping.paths) {
                    tree.put(glob, rules);
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
    RuleMap<BindKey, List<InjectorBuilder>> getRenderers() {
        return renderers.newRuleMap();
    }
}
