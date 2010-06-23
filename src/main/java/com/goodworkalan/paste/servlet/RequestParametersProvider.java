package com.goodworkalan.paste.servlet;

import javax.inject.Inject;
import javax.inject.Provider;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Request;

/**
 * A parameters provider that simply returns parameters qualified
 * with the <code>Request</code> annotation. This is used to provide
 * the parameters qualified with the <code>Filter</code> annotation,
 * when the filter invocation is the first filter invocation, and the 
 * filter parameters are the same as the request parameters. 
 *
 * @author Alan Gutierrez
 */
public class RequestParametersProvider implements Provider<Parameters> {
    /** The request parameters. */
    private final Parameters parameters;

    /**
     * Create a request parameters provider using the given request parameters.
     * 
     * @param parameters
     *            The request parameters.
     */
    @Inject
    public RequestParametersProvider(@Request Parameters parameters) {
        this.parameters = parameters;
    }

    /**
     * Get the request parameters.
     * 
     * @return The request parameters.
     */
    public Parameters get() {
        return parameters;
    }
}
