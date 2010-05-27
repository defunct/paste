package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Controller;

public class ControllerParameters {
    @Inject
    public ControllerParameters(@Controller Parameters parameters, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(parameters.get("a").getName());
        response.getWriter().println(parameters.get("b").getName());
        response.getWriter().println(parameters.get("c").getName());
    }
}
