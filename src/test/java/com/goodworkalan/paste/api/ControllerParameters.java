package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Parameters;
import com.goodworkalan.paste.controller.qualifiers.Controller;

/**
 * A controller to test controller parameters, URL parameter capturing.
 *
 * @author Alan Gutierrez
 */
public class ControllerParameters {
    /**
     * Check that the parameters are captured.
     * 
     * @param parameters
     *            The controller parameters.
     * @param response
     *            The response.
     * @throws IOException
     *             For any I/O error.
     */
    @Inject
    public ControllerParameters(@Controller Parameters parameters, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(parameters.get("a"));
        response.getWriter().println(parameters.get("b"));
        response.getWriter().println(parameters.get("c"));
    }
}
