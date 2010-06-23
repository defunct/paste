package com.goodworkalan.paste.servlet;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.controller.NamedValue;
import com.goodworkalan.paste.controller.Parameters;

/**
 * Gets the parameters parsed by the HTTP Servlet engine available through the
 * <code>HttpServletRequest</code>.
 * <p>
 * This is how we obtain the parameters set to the request, for the request
 * scoped list of parameters. There is a different strategy for obtaining the
 * parameters for forwarded or included invocations of the filter.
 * <p>
 * The list of parameters is stored in the request scope, so that the servlet
 * parameters enumeration is only traversed once and the results cached.
 * 
 * @author Alan Gutierrez
 */
public class EnumeratedParametersProvider implements Provider<Parameters> {
    /** The HTTP request. */
    private final HttpServletRequest request;

    /**
     * Create an enumerated parameters provider.
     * 
     * @param request
     *            The HTTP request.
     */
    @Inject
    public EnumeratedParametersProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get the enumerated parameters as a list of parameters.
     * 
     * @return A list of request parameters read from the
     *         <code>HttpServletRequest</code> parameter enumeration.
     */
    public Parameters get() {
        Parameters parameters = new Parameters();
        Enumeration<?> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            for (String value : request.getParameterValues(name)) {
                parameters.add(new NamedValue(name, value));
            }
        }
        return parameters;
    }
}
