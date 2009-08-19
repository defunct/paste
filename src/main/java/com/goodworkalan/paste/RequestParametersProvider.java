package com.goodworkalan.paste;

import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the parameters for the first invocation of the Paste filter for this
 * request.
 * 
 * @author Alan Gutierrez
 */
public class RequestParametersProvider implements Provider<Parameters> {
    /**
     * The filtration for the first invocation of the Paste filter for this
     * request.
     */
    private final Filtration filtration;

    /**
     * Create a provider that provides the parameters for the first invocation
     * of the Paste filter for this request.
     * 
     * @param criteria
     *            The criteria for the first invocation of the Paste filter for
     *            this request.
     */
    @Inject
    public RequestParametersProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get the parameters for the first invocation of the Paste filter for this
     * request.
     * 
     * @return The parameters for the first invocation of the Paste filter for
     *         this request.
     */
    public Parameters get() {
        return filtration.getParameters();
    }
}
