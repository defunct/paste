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
    // TODO Document.
    private final Parameters parameters;
    // TODO Document.
    private final Converter converter;
    // TODO Document.
    private final Injector injector;
    // TODO Document.
    private final List<StashAssignment<?>> assignments;
    // TODO Document.
    private final Object controller;
    
    /**
     * Construct an infusion actor with the given controller parameters.
     * 
     * @param parameters
     *            Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(@Controller Parameters parameters, Converter converter, Injector injector, List<StashAssignment<?>> assignments, @Controller Object controller) {
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