package com.goodworkalan.paste.infuse;

import java.util.List;

import javax.inject.Inject;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.stringbeans.Converter;
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

    /**
     * The String Beans <code>Converter</code> that defines conversion
     * strategies from strings to specific Java types.
     */
    private final Converter converter;

    /** The dependency injector. */
    private final Injector injector;

    /**
     * The list of objects to obtain from the <code>Injector</code> and assign
     * to the <code>Stash</code> of the StringBeans <code>UrlParser</code>.
     */
    private final List<StashAssignment<?>> assignments;

    /** The controller. */
    private final Object controller;

    /**
     * Construct an infusion actor with the given controller parameters.
     * 
     * @param injector
     *            The dependency injector.
     * @param converter
     *            The String Beans <code>Converter</code> that defines
     *            conversion strategies from strings to specific Java types.
     * @param assignments
     *            The list of objects to obtain from the <code>Injector</code>
     *            and assign to the <code>Stash</code> of the StringBeans
     *            <code>UrlParser</code>.
     * @param controller
     *            The controller.
     * @param parameters
     *            Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(Injector injector, Converter converter,
            List<StashAssignment<?>> assignments,
            @Controller Object controller, @Controller Parameters parameters) {
        this.parameters = parameters;
        this.converter = converter;
        this.injector = injector;
        this.assignments = assignments;
        this.controller = controller;
    }

    // TODO Document.
    public void run() {
        UrlParser parser = new UrlParser(converter);
        for (StashAssignment<?> assignment : assignments) {
            assignment.assign(injector, parser.getStash());
        }
        parser.populate(controller, parameters.toStringMap(true));
    }
}