package com.goodworkalan.paste.infuse;

import java.util.Set;

import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.util.Parameters;
import com.goodworkalan.stringbeans.Stringer;
import com.goodworkalan.stringbeans.url.UrlParser;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * An actor that sets bean properties in a controller.
 * <p>
 * Note that is would be simple to support the array[] parameter name notation
 * but we choose not to for consistency. If people tell me that it is dearly
 * missed, then it can be added.
 * 
 * @author Alan Gutierrez
 */
public class InfusionActor implements Actor {
    /** The controller parameters. */
    private final Parameters parameters;

    /** The string beans configuration. */
    private final Stringer stringer;
    
    private final Injector injector;
    
    private final Set<StashAssignment<?>> assignments;

    /**
     * Construct an infusion actor with the given controller parameters.
     * 
     * @param parameters
     *            Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(@Controller Parameters parameters, Stringer stringer, Injector injector, @Infusable Set<StashAssignment<?>> assignments) {
        this.parameters = parameters;
        this.stringer = stringer;
        this.injector = injector;
        this.assignments = assignments;
    }

    // FIXME Document.
    public Throwable actUpon(Object controller) {
        UrlParser parser = new UrlParser(stringer, true);
        for (StashAssignment<?> assignment : assignments) {
            assignment.assign(injector, parser.getStash());
        }
        parser.populate(controller, parameters.toStringMap(true));
        return null;
    }
}