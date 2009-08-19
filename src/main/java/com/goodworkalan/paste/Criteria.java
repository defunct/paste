package com.goodworkalan.paste;

import javax.servlet.http.HttpServletRequest;

/**
 * A wrapper around a request that extracts the request criteria -- the paths
 * and parameters -- for an include or forward, if the request contains criteria
 * for an include or forward.
 * 
 * @author Alan Gutierrez
 */
public class Criteria {
    /** The request URI. */
    private final String requestUri;
    
    /** The servlet path. */
    private final String servletPath;
    
    /** The context path. */
    private final String contextPath;
    
    /** The extra path info. */
    private final String pathInfo;
    
    /** The query string. */
    private final String queryString;
    
    /**
     * Extract criteria from the given request.
     * 
     * @param request The request.
     */
    public Criteria(HttpServletRequest request) {
        String requestUri = (String) request.getAttribute("javax.servlet.include.request_uri");
        if (requestUri == null) {
            requestUri = request.getRequestURI();
        }
        this.requestUri = requestUri;
        String contextPath = (String) request.getAttribute("javax.servlet.include.context_path");
        if (contextPath == null) {
            contextPath = request.getContextPath();
        }
        this.contextPath = contextPath;
        String servletPath = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (servletPath == null) {
            servletPath = request.getPathInfo();
        }
        this.servletPath = servletPath;
        String pathInfo = (String) request.getAttribute("javax.servlet.include.path_info");
        if (pathInfo == null) {
            pathInfo = request.getServletPath();
        }
        this.pathInfo = pathInfo;
        String queryString =  (String) request.getAttribute("javax.servlet.include.query_string");
        if (queryString == null) {
            queryString = request.getQueryString();
        }
        this.queryString = queryString;

    }

    /**
     * Get the request URI specific to this filter invocation, returning the
     * nested forward or include context path if applicable.
     * 
     * @return The request URI.
     */
    public String getRequestURI() {
        return requestUri;
    }

    /**
     * Get the context path specific to this filter invocation, returning the
     * nested forward or include context path if applicable.
     * 
     * @return The context path.
     */
    public String getContextPath() {
        return contextPath;
    }

    /**
     * Get the servlet path specific to this filter invocation, returning the
     * nested forward or include servlet path if applicable.
     * 
     * @return The servlet path.
     */
    public String getPathInfo() {
        return servletPath;
    }

    /**
     * Get any extra path info specific to this filter invocation, returning the
     * nested forward or extra path info if applicable.
     * 
     * @return Any extra path info.
     */
    public String getServletPath() {
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
    public String getQueryString() {
        return queryString;
    }
}
