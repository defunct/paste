package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Criteria;
import com.google.inject.Inject;

public class Included {
    @Inject
    public Included(Criteria criteria, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(criteria.getContextPath());
        response.getWriter().println(criteria.getServletPath());
        response.getWriter().println(criteria.getRequestURI());
        response.getWriter().println(criteria.getQueryString());
        response.getWriter().println(criteria.getPathInfo());
    }
}
