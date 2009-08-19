package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Key;

/**
 * A structure that contains the scopes for a single filtration.
 *
 * @author Alan Gutierrez
 */
public class Filtration {
    /** The paths and query string for this invocation of the Paste filter. */
    private final Criteria criteria;
    
    /** The servlet request. */
    private final HttpServletRequest request;
    
    /** The servlet response. */
    private final HttpServletResponse response;
    
    /** The map that backs the filter scope. */
    private final Map<Key<?>, Object> filterScope;
    
    /** The map that backs the controller scope. */
    private final Map<Key<?>, Object> controllerScope;
    
    /** The list of janitors. */
    private final List<Janitor> janitors;
    
    /** The lazily initialized parameters. */
    private Parameters parameters;
    
    /** If true, subsequent calls to the filter have been made. */
    private boolean subsequent;

    /**
     * Create a new filtration structure with the given servlet request and the
     * given servlet response.
     * 
     * @param request
     *            The servlet request.
     * @param response
     *            The servlet response.
     */
    public Filtration(HttpServletRequest request, HttpServletResponse response) {
        this.criteria = new Criteria(request);
        this.request = request;
        this.response = response;
        this.filterScope = new HashMap<Key<?>, Object>();
        this.controllerScope = new HashMap<Key<?>, Object>();
        this.janitors = new ArrayList<Janitor>();
    }

    /**
     * Get the paths and query string for this invocation of the Paste filter.
     * 
     * @return The paths and query string for this invocation of the Paste
     *         filter.
     */
    public Criteria getCriteria() {
        return criteria;
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
     * Tells this filtration that a subsequent call to the paste filter has been
     * made during a request. Subsequent calls will change the state of the request so that
     * the parameters may change. If a controller handling forward or include
     * wants the request parameters, they will need to be cached by injecting
     * the parameters into a controller during the first request.
     */
    public void setSubsequent() {
        subsequent = true;
    }

    /**
     * Get the parameters for this filtration. Parameters are lazily initialized
     * to prevent reading non form encoded POSTs (and PUTs). If you do not want
     * the body of a request interpreted as form parameters, do not inject
     * parameters into the controller for that request.
     * <p>
     * Subsequent calls will change the state of the request so that the
     * parameters may change. If a controller handling forward or include wants
     * the request parameters, the request parameters will need to be cached by
     * injecting the parameters into a controller during the first request.
     * 
     * @return The parameters.
     */
    public Parameters getParameters() {
        if (subsequent) {
            throw new PasteException();
        }
        if (parameters == null) {
            if (request.getAttribute("javax.servlet.include.request_uri") != null) {
                String query = (String) request.getAttribute("javax.servlet.include.query_string");
                if (query == null) {
                    query = "";
                }
                parameters = Parameters.fromQueryString(query, NamedValue.REQUEST);
            } else {
                List<NamedValue> namedValues = new ArrayList<NamedValue>();
                Enumeration<String> names = Objects.toStringEnumeration(request.getParameterNames());
                while (names.hasMoreElements()) {
                    String name = names.nextElement();
                    for (String value : request.getParameterValues(name)) {
                        namedValues.add(new NamedValue(NamedValue.REQUEST, name, value));
                    }
                }
                parameters = new Parameters(namedValues);
            }
        }
        return parameters;
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
     * Get the list of per filter invocation janitors.
     * 
     * @return The list of per filter invocation janitors.
     */
    List<Janitor> getJanitors() {
        return janitors;
    }
}
