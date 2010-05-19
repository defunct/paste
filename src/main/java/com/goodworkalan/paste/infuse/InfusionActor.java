package com.goodworkalan.paste.infuse;

import java.util.Set;

import javax.inject.Inject;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.util.Parameters;
import com.goodworkalan.stringbeans.Stringer;
import com.goodworkalan.stringbeans.url.UrlParser;

/**
 * An actor that sets bean properties in a controller.
 * <p>
 * Note that is would be simple to support the array[] parameter name notation
 * but we choose not to for consistency. If people tell me that it is dearly
 * missed, then it can be added.
 * 
 * @author Alan Gutierrez
 */
public class InfusionActor implements Runnable {
    /** The controller parameters. */
    private final Parameters parameters;

    /** The string beans configuration. */
    private final Stringer stringer;
    
    private final Injector injector;
    
    private final Set<StashAssignment<?>> assignments;
    
    private final Object controller;

    /**
     * Construct an infusion actor with the given controller parameters.
     * 
     * @param parameters
     *            Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(@Controller Parameters parameters, Stringer stringer, Injector injector, @Infusable Set<StashAssignment<?>> assignments, @Controller Object controller) {
        this.parameters = parameters;
        this.stringer = stringer;
        this.injector = injector;
        this.assignments = assignments;
        this.controller = controller;
    }

    // FIXME Document.
    public void run() {
        UrlParser parser = new UrlParser(stringer);
        for (StashAssignment<?> assignment : assignments) {
            assignment.assign(injector, parser.getStash());
        }
        parser.populate(controller, parameters.toStringMap(true));
    }
}