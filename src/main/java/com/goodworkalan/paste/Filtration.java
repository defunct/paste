package com.goodworkalan.paste;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.janitor.Janitor;
import com.google.inject.Key;

/**
 * A structure that contains the scopes for a single filtration.
 *
 * @author Alan Gutierrez
 */
public class Filtration {
    /** If true, this is the first invocation of the filter. */
    private final boolean first;

    /** The servlet request. */
    private final HttpServletRequest request;
    
    /** The servlet response. */
    private final HttpServletResponse response;
    
    /** The map that backs the filter scope. */
    private final Map<Key<?>, Object> filterScope;
    
    /** The map that backs the controller scope. */
    private final Map<Key<?>, Object> controllerScope;
    
    /** The list of per request janitors. */
    private final List<Janitor> requestJanitors;
    
    /** The list of per filter janitors. */
    private final List<Janitor> filterJanitors;

    /**
     * Create a new filtration structure with the given servlet request and the
     * given servlet response.
     * 
     * @param request
     *            The servlet request.
     * @param response
     *            The servlet response.
     */
    public Filtration(boolean first, HttpServletRequest request, HttpServletResponse response, List<Janitor> requestJanitors, List<Janitor> filterJanitors) {
        this.first = first;
        this.request = request;
        this.response = response;
        this.filterScope = new HashMap<Key<?>, Object>();
        this.controllerScope = new HashMap<Key<?>, Object>();
        this.requestJanitors = requestJanitors;
        this.filterJanitors = filterJanitors;
    }

    /**
     * Get whether or not the filtration is the first invocation of the filter.
     * 
     * @return True if this filtration is the first invocation of the filter.
     */
    public boolean isFirst() {
        return first;
    }

    /**
     * Get the servlet request.
     * 
     * @return The servlet request.
     */
    public HttpServletRequest getRequest() {
        return request;
    }
    
    /**
     * Get the response.
     * 
     * @return The response.
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Get the map that backs the filter scope.
     * 
     * @return The map that backs the filter scope.
     */
    Map<Key<?>, Object> getFilterScope() {
        return filterScope;
    }

    /**
     * Get the map that backs the controller scope.
     * 
     * @return The map that backs the controller scope.
     */
    Map<Key<?>, Object> getControllerScope() {
        return controllerScope;
    }

    /**
     * Get the list of per request janitors.
     * 
     * @return The list of per request janitors.
     */
    List<Janitor> getRequestJanitors() {
        return requestJanitors;
    }

    /**
     * Get the list of per filter janitors.
     * 
     * @return The list of per filter janitors.
     */
    List<Janitor> getFilterJanitors() {
        return filterJanitors;
    }
}
