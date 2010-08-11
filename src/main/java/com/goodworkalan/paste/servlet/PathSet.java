package com.goodworkalan.paste.servlet;

import java.util.List;

import com.goodworkalan.dovetail.PathAssociation;
import com.goodworkalan.paste.cassette.BindKey;
import com.goodworkalan.winnow.RuleMap;

/**
 * A set of path to rule map associations used to match paths to the rule sets
 * that will further winnow down the choice of controller.
 * 
 * @author Alan Gutierrez
 */
class PathSet {
    /** Whether or not to include the suffix in the path. */
    public final boolean includeSuffix;
    
    /** The list of path to rule map associations. */
    public final List<PathAssociation<RuleMap<BindKey, Class<?>>>> pathAssociations;

    /**
     * Create a connector.
     * 
     * @param pathAssociations
     *            The list of path to rule map associations.
     * @param includeSuffix
     *            Whether or not to include the suffix in the path.
     */
    public PathSet(List<PathAssociation<RuleMap<BindKey, Class<?>>>> pathAssociations, boolean includeSuffix) {
        this.pathAssociations = pathAssociations;
        this.includeSuffix = includeSuffix;
    }
}
