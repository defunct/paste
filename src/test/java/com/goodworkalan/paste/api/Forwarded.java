package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Criteria;
import com.goodworkalan.paste.Request;
import com.google.inject.Inject;

public class Forwarded {
    @Inject
    public Forwarded(@Request Criteria request, Criteria filter, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(request.getPath());
        response.getWriter().println(filter.getPath());
    }
}
