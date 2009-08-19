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
    /** The filtration for the current invocation of the paste filter. */
    private final Filtration filtration;

    /**
     * Create a provider that provide thes parameters specific to the current
     * invocation of the paste filter.
     * 
     * @param filtration
     *            The filtration for the current invocation of the paste filter.
     */
    @Inject
    public FilterParametersProvider(@Filter Filtration filtration) {
        this.filtration = filtration;
    }

    /**
     * Get the parameters specific to the current invocation of the paste
     * filter.
     * 
     * @return The parameters specific to the current invocation of the paste
     *         filter.
     */
    public Parameters get() {
        return filtration.getParameters();
    }
}
