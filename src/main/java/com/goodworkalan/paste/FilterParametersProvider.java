package com.goodworkalan.paste;

import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * Provide the parameters specific to the current invocation of the paste
 * filter.
 * 
 * @author Alan Gutierrez
 */
public class FilterParametersProvider implements Provider<Parameters> {
    /** The criteria for the current invocation of the paste filter. */
    private final Criteria criteria;

    /**
     * Provide the parameters specific to the current invocation of the paste
     * filter.
     * 
     * @author Alan Gutierrez
     */
    @Inject
    public FilterParametersProvider(@Filter Criteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Get the parameters specific to the current invocation of the paste
     * filter.
     * 
     * @return The parameters specific to the current invocation of the paste
     *         filter.
     */
    public Parameters get() {
        return criteria.getParameters();
    }
}
