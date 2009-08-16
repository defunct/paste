package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Inject;
import com.google.inject.Provider;

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
        List<NamedValue> parameters = new ArrayList<NamedValue>();
        for (Map.Entry<String, String> entry : controllerMappings.entrySet()) {
            parameters.add(new NamedValue(NamedValue.CONTROLLER,
                    entry.getKey(), entry.getValue()));
        }

        for (NamedValue namedValue : filterParameters) {
            parameters.add(namedValue);
        }
        return new Parameters(parameters);
    }
}
