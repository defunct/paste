package com.goodworkalan.paste;

import com.google.inject.Provider;

/**
 * Provides the filtration created for the current invocation of the paste
 * filter.
 * 
 * @author Alan Gutierrez
 */
public class FilterFiltrationProvider implements Provider<Filtration> {
    /**
     * Get the filtration created for the current invocation of the paste
     * filter.
     * 
     * @return The filtration created for the current invocation of the paste
     *         filter.
     */
    public Filtration get() {
        return PasteGuicer.getFilterFiltration();
    }
}
