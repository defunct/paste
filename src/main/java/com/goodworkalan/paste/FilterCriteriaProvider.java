package com.goodworkalan.paste;

import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the criteria specific to the current invocation of the paste filter.
 * 
 * @author Alan Gutierrez
 */
public class FilterCriteriaProvider implements Provider<Criteria> {
    /** The filtration created for the current invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a criteria provider that will return the criteria specific to the
     * current invocation of the paste filter.
     * 
     * @param filtration
     *            The filtration created for the current invocation of the paste
     *            filter.
     */
    @Inject
    public FilterCriteriaProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get the criteria specific to the current invocation of the paste filter.
     * 
     * @return The criteria specific to the current invocation of the paste
     *         filter.
     */
    public Criteria get() {
        return new Criteria(filtration.getRequest());
    }
}
