package com.goodworkalan.paste;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provide the criteria for the first request filtered by the Paste filter. This
 * is used by controllers handling forwards or includes requests to obtain the
 * criteria of the original HTTP request. In order for this to work as expected,
 * the Paste filter must filter the original HTTP request.
 * 
 * @author Alan Gutierrez
 */
public class RequestCriteriaProvider implements Provider<Criteria> {
    /**
     * The filtration created for the first invocation of the Paste filter for
     * this request.
     */
    private final Filtration filtration;

    /**
     * Create a provider that provides the criteria for the first request
     * filtered by the Paste filter.
     * 
     * @param filtration
     *            The filtration created for the first invocation of the Paste
     *            filter for this request.
     */
    @Inject
    public RequestCriteriaProvider(@Request Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get the criteria for the first request filtered by the Paste filter.
     * 
     * @return The criteria for the first request filtered by the Paste filter.
     */
    public Criteria get() {
        return filtration.getCriteria();
    }
}
