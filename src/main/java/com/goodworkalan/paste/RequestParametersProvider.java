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
     * The criteria for the first invocation of the Paste filter for this
     * request.
     */
    private final Criteria criteria;

    /**
     * Create a provider that provides the parameters for the first invocation
     * of the Paste filter for this request.
     * 
     * @param criteria The criteria for the first invocation of the Paste filter for this
     * request.
     */
    @Inject
    public RequestParametersProvider(@Request Criteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Get the parameters for the first invocation of the Paste filter for this
     * request.
     * 
     * @return The parameters for the first invocation of the Paste filter for
     *         this request.
     */
    public Parameters get() {
        return criteria.getParameters();
    }
}
