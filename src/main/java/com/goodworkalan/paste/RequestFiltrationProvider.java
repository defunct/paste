package com.goodworkalan.paste;

import com.google.inject.Provider;

/**
 * Provides the filtration structure specific to the first filter invocation.
 * 
 * @author Alan Gutierrez
 */
public class RequestFiltrationProvider implements Provider<Filtration> {
    /**
     * Get the filtration structure specific to the first filter invocation.
     * 
     * @return The filtration structure specific to the first filter
     *         invocation.
     */
    public Filtration get() {
        return PasteGuicer.getReactionFiltration();
    }
}
