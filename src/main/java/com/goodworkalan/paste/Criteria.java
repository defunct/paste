package com.goodworkalan.paste;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.util.NamedValue;
import com.goodworkalan.paste.util.Parameters;

/**
 * A wrapper around a request that extracts the request criteria -- the paths
 * and parameters -- for an include or forward, if the request contains criteria
 * for an include or forward.
 * 
 * @author Alan Gutierrez
 */
public class Criteria {
    /** The HTTP request. */
    private final HttpServletRequest request;
    
    /**
     * Extract criteria from the given request.
     * 
     * @param request The request.
     */
    public Criteria(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Get the request URI specific to this filter invocation, returning the
     * nested forward or include context path if applicable.
     * 
     * @return The request URI.
     */
    public String getRequestURI() {
        String requestUri = (String) request.getAttribute("javax.servlet.forward.request_uri");
        if (requestUri == null) {
            requestUri = (String) request.getAttribute("javax.servlet.include.request_uri");
            if (requestUri == null) {
                requestUri = request.getServletPath();
            }
        }
        return requestUri;
    }

    /**
     * Get the context path specific to this filter invocation, returning the
     * nested forward or include context path if applicable.
     * 
     * @return The context path.
     */
    public String getContextPath() {
        String contextPath = (String) request.getAttribute("javax.servlet.forward.context_path");
        if (contextPath == null) {
            contextPath = (String) request.getAttribute("javax.servlet.include.context_path");
            if (contextPath == null) {
                contextPath = request.getServletPath();
            }
        }
        return contextPath;
    }

    /**
     * Get the servlet path specific to this filter invocation, returning the
     * nested forward or include servlet path if applicable.
     * 
     * @return The servlet path.
     */
    public String getPathInfo() {
        String servletPath = (String) request.getAttribute("javax.servlet.forward.servlet_path");
        if (servletPath == null) {
            servletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");
            if (servletPath == null) {
                servletPath = request.getServletPath();
            }
        }
        return servletPath;
    }

    /**
     * Get any extra path info specific to this filter invocation, returning the
     * nested forward or extra path info if applicable.
     * 
     * @return Any extra path info.
     */
    public String getServletPath() {
        String pathInfo = (String) request.getAttribute("javax.servlet.forward.path_info");
        if (pathInfo == null) {
            pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
            if (pathInfo == null) {
                pathInfo = request.getPathInfo();
            }
        }
        return pathInfo;
    }

    /**
     * Get the path used to match against our routes, a path stripped of the
     * servlet context.
     * 
     * @return The path use to match against routes.
     */
    public String getPath() {
        String path = getRequestURI();
        String contextPath = getContextPath();
        if (contextPath != null) {
            path = path.substring(contextPath.length());
        }
        return path;
    }
    
    /**
     * Get the parameters specific to this filter invocation.
     * 
     * @return The parameters specific to this filter invocation.
     */
    public Parameters getParameters() {
        String query = (String) request.getAttribute("javax.servlet.forward.query_string");
        if (query == null) {
            query = (String) request.getAttribute("javax.servlet.include.query_string");
        }
        if (query == null) {
            List<NamedValue> parameters = new ArrayList<NamedValue>();
            Enumeration<String> names = Objects.toStringEnumeration(request.getParameterNames());
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                for (String value : request.getParameterValues(name)) {
                    parameters.add(new NamedValue(NamedValue.REQUEST, name, value));
                }
            }
            return new Parameters(parameters);
        }
        return Parameters.fromQueryString(query, NamedValue.REQUEST);
    }
}
