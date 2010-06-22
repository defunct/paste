package com.goodworkalan.paste.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.winnow.RuleMap;
import com.goodworkalan.winnow.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.Routes;
import com.goodworkalan.paste.controller.qualifiers.Controller;

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
    // TODO Document.
    public static final class Connection {
        // TODO Document.
        public List<Glob> globs;
        
        // TODO Document.
        public RuleMapBuilder<ControllerCandidate> rules;
        
        // TODO Document.
        public Connection(List<Glob> globs, RuleMapBuilder<ControllerCandidate> rules) {
            this.globs = globs;
            this.rules = rules;
        }
    }
    
    // TODO Document.
    public static final class ControllerCandidate {
        // TODO Document.
        public int priority;
        
        // TODO Document.
        public Class<?> controllerClass;
        
        // TODO Document.
        public ControllerCandidate(int priority, Class<?> controllerClass) {
            this.priority = priority;
            this.controllerClass = controllerClass;
        }
    }
    
    // TODO Document.
    public static final class RenderCandidate {
        // TODO Document.
        public int priority;
        
        // TODO Document.
        public List<InjectorBuilder> modules;

        // TODO Document.
        public RenderCandidate(int priority, List<InjectorBuilder> modules) {
            this.priority = priority;
            this.modules = modules;
        }
    }

    /** A map of controller classes to the globs that define their URL bindings. */
    public Map<Class<?>, Glob> routes;

    /**
     * A list of connection groups, a connection group consisting of URL
     * bindings. URL bindings consisting of a list of globs paired with rule map
     * of tests to further winnow the match based on request parameters.
     */
    public List<List<Connection>> connections;
    
    /** A rule map to match a controller or exception to a renderer. */
    public RuleMapBuilder<RenderCandidate> renderers;
    
    /** The map of annotations to controllers. */
    public Map<Class<?>, List<Class<?>>> reactions;

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
    Map<Class<?>, Glob> getRoutes() {
        return routes;
    }

    /**
     * Builds a list of connection groups, where a connection group is a glob
     * tree that maps to a rule map of tests to further winnow the match based
     * on request parameters. This method builds the glob trees from their
     * intermediate state, which is the list of globs to add to the glob tree. 
     * 
     * @return The list of connection groups.
     */
    List<GlobTree<RuleMap<ControllerCandidate>>> getConnections() {
        List<GlobTree<RuleMap<ControllerCandidate>>> trees = new ArrayList<GlobTree<RuleMap<ControllerCandidate>>>();
        for (List<Connection> listOfControllerPathMappings : connections) {
            GlobTree<RuleMap<ControllerCandidate>> tree = new GlobTree<RuleMap<ControllerCandidate>>();
            for (Connection mapping : listOfControllerPathMappings) {
                RuleMap<ControllerCandidate> rules = mapping.rules.newRuleMap();
                for (Glob glob : mapping.globs) {
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
    RuleMap<RenderCandidate> getRenderers() {
        return renderers.newRuleMap();
    }
}
