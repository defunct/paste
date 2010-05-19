package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.util.Parameters;

public class ControllerParameters {
    @Inject
    public ControllerParameters(@Controller Parameters parameters, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(parameters.getFirst("a"));
        response.getWriter().println(parameters.getFirst("b"));
        response.getWriter().println(parameters.getFirst("c"));
    }
}
