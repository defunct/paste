package com.goodworkalan.paste.infuse;

import java.util.Map;

import javax.inject.Inject;

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

    /**
     * The heterogeneous container of unforeseen participants in the
     * construction of beans in the object graph.
     */
    private final Map<Object, Object> participants;

    /** The controller. */
    private final Object controller;

    /**
     * Construct an infusion actor with the given controller parameters.
     * 
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
    public InfusionActor(Converter converter, @Infusable Map<Object, Object> participants,
            @Controller Object controller, @Controller Parameters parameters) {
        this.parameters = parameters;
        this.converter = converter;
        this.participants = participants;
        this.controller = controller;
    }

    /**
     * Infuse the controller with objects extracted from the request parameters.
     */
    public void run() {
        UrlParser parser = new UrlParser(converter);
        parser.getParticipants().putAll(participants);
        parser.populate(controller, parameters.toStringMap(true));
    }
}