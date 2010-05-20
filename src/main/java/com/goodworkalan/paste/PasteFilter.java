package com.goodworkalan.paste;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.janitor.Janitor;

/**
 * A filter that dispatches requests to controllers by matching the request path
 * against URL bindings.
 * 
 * @author Alan Gutierrez
 */
public class PasteFilter implements Filter {
    /** The serial version id. */
    private static final long serialVersionUID = 20081122L;

    /** The implementation delegate. */
    private Responder responder;

    /**
     * Initialize the Paste filter.
     * 
     * @param config
     *            The filter configuration.
     */
    public void init(FilterConfig config) throws ServletException {
        Map<String, String> initialization = new HashMap<String, String>();
        Enumeration<?> e = config.getInitParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement().toString();
            initialization.put(name, config.getInitParameter(name));
        }
        responder = new Responder(config.getServletContext(), initialization);
        responder.start();
    }

    /**
     * Filter the given request and given response forwarding the request to the
     * given filter chain if none of our controllers intercepts the request and
     * sends a response of its own.
     * 
     * @param request
     *            The request.
     * @param response
     *            The response.
     * @param chain
     *            The remaining filter chain.
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
    throws IOException, ServletException {
        responder.filter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    /**
     * Destroy the filter by invoking an web application wide {@link Janitor}
     * instances.
     */
    public void destroy() {
        responder.destroy();
    }
}

/* vim: set et tw=80 nowrap: */
