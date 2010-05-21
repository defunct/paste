package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Criteria;
import com.goodworkalan.paste.qualifiers.Request;

public class Forwarded {
    @Inject
    public Forwarded(@Request Criteria original, Criteria filter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(original.getPath());
        response.getWriter().println(filter.getPath());
        response.getWriter().println(request.getAttribute("steve") instanceof Forwarding);
    }
}
