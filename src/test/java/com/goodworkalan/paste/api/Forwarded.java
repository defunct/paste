package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.qualifiers.Request;

/**
 * Write details of a forwarded response for inspection during testing.
 *
 * @author Alan Gutierrez
 */
public class Forwarded {
    /**
     * Write a forwarded response.
     * 
     * @param original
     *            The request criteria.
     * @param filter
     *            The filter criteria.
     * @param request
     *            The request.
     * @param response
     *            The response.
     * @throws IOException
     *             For any I/O error.
     */
    @Inject
    public Forwarded(@Request Criteria original, Criteria filter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(original.getPath());
        response.getWriter().println(filter.getPath());
        response.getWriter().println(request.getAttribute("steve") instanceof Forwarding);
    }
}
