package com.goodworkalan.paste.controller;

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
        boolean isInclude = isInclude(request, "request_uri", "context_path", "servlet_path", "path_info", "query_string");
        this.requestUri =
                isInclude
                    ? (String)request.getAttribute("javax.servlet.include.request_uri")
                    : request.getRequestURI();
        this.contextPath =
            cleanContextPath(
                isInclude
                    ? (String) request.getAttribute("javax.servlet.include.context_path")
                    : request.getContextPath());
        this.servletPath =
            isInclude
                ? (String) request.getAttribute("javax.servlet.include.servlet_path")
                : request.getPathInfo();
        this.pathInfo =
            isInclude
                ? (String) request.getAttribute("javax.servlet.include.path_info")
                : request.getServletPath();
        this.queryString =
            isInclude
                ? (String) request.getAttribute("javax.servlet.include.query_string")
                : request.getQueryString();
    }

    /**
     * During include, Jetty changes the context path into a single slash, when
     * it is an empty string during request or forward. This method removes a
     * trailing slash in any case.
     * 
     * @param contextPath
     *            The context path.
     * @return The context path less any trailing slash.
     */
    private static String cleanContextPath(String contextPath) { 
        return contextPath.endsWith("/") ? contextPath.substring(0, contextPath.length() - 1) : contextPath;
    }

    /**
     * An exhaustive test to see if this is filter invocation is for an include.
     * 
     * @param request
     *            The request.
     * @param properties
     *            The properties that might be present if this invocation is for
     *            an include.
     * @return True if any of the properties is not null.
     */
    // FIXME What if an include forwards? Are they both there?
    private static boolean isInclude(HttpServletRequest request, String...properties) {
        for (String property : properties) {
            if (request.getAttribute("javax.servlet.include." + property) != null) {
                return true;
            }
        }
        return false;
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
        return getRequestURI().substring(getContextPath().length());
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
