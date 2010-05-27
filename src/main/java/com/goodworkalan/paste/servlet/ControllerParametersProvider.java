package com.goodworkalan.paste.servlet;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.paste.controller.NamedValueList;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.qualifiers.Filter;

/**
 * Provide the parameters specific to the current controller.
 * 
 * @author Alan Gutierrez
 */
public class ControllerParametersProvider implements Provider<Parameters> {
    /**  The filter mappings from the last matched controller. */
    private final Map<String, String> controllerMappings;
    
    /** The parameters specific to the current filter invocation. */
    private final Parameters filterParameters;

    /**
     * Create a provider that provides parameters specific to the current
     * controller.
     * 
     * @param controllerMappings
     *            The filter mappings from the last matched controller.
     * @param filterParameters
     *            The parameters specific to the current filter invocation.
     */
    @Inject
    public ControllerParametersProvider(@Controller Map<String, String> controllerMappings, @Filter Parameters filterParameters) {
        this.controllerMappings = controllerMappings;
        this.filterParameters = filterParameters;
    }

    /**
     * Get the parameters specific to the current controller.
     * 
     * @return The parameters specific to the current controller.
     */
    public Parameters get() {
        return new Parameters(new NamedValueList(controllerMappings), filterParameters);
    }
}
