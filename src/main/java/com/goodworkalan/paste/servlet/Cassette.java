package com.goodworkalan.paste.servlet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.deviate.RuleMap;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.dovetail.GlobTree;
import com.goodworkalan.ilk.association.IlkAssociation;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.Routes;
import com.goodworkalan.paste.qualifiers.Controller;
import com.mallardsoft.tuple.Pair;
import com.mallardsoft.tuple.Tuple;

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
public class Cassette {
    /** A map of controller classes to the globs that define their URL bindings. */
    public Map<Class<?>, Glob> controllerToGlob;

    /**
     * A list of connection groups, a connection group consisting of URL
     * bindings. URL bindings consisting of a list of globs paired with rule map
     * of tests to further winnow the match based on request parameters.
     */
    public List<List<Pair<List<Glob>, RuleMapBuilder<Pair<Integer, Class<?>>>>>> connections;
    
    /** A rule map to match a controller or exception to a renderer. */
    public RuleMapBuilder<Pair<Integer, List<InjectorBuilder>>> viewRules;
    
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
     * Create a routes object that will return the controller mapped to a given
     * URL.
     * 
     * @return A controller lookup.
     */
    Routes getRoutes() {
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
    List<GlobTree<RuleMap<Pair<Integer, Class<?>>>>> getBindingTrees() {
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
    RuleMap<Pair<Integer, List<InjectorBuilder>>> getViewRules() {
        return viewRules.newRuleMap();
    }
}
