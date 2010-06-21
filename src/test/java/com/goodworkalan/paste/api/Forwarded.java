package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Criteria;
import com.goodworkalan.paste.controller.qualifiers.Request;

// TODO Document.
public class Forwarded {
    // TODO Document.
    @Inject
    public Forwarded(@Request Criteria original, Criteria filter, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(original.getPath());
        response.getWriter().println(filter.getPath());
        response.getWriter().println(request.getAttribute("steve") instanceof Forwarding);
    }
}
