package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Request;

// TODO Document.
public class Included {
    // TODO Document.
    @Inject
    public Included(@Request Parameters original, Parameters parameters, Criteria criteria, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(criteria.getContextPath());
        response.getWriter().println(criteria.getServletPath());
        response.getWriter().println(criteria.getRequestURI());
        response.getWriter().println(criteria.getQueryString());
        response.getWriter().println(criteria.getPathInfo());
        if ("true".equals(original.get("qs"))) {
            response.getWriter().println(parameters.get("a"));
            response.getWriter().println(parameters.get("b"));
        }
    }
}
