package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Parameters;

/**
 * A controller to test including other requests from within the Servlet engine
 * using the <code>RequestDispatcher</code>.
 * 
 * @author Alan Gutierrez
 */
public class Including {
    /**
     * Use the <code>RequestDispatcher</code> to request an included controller.
     * 
     * @param parameters
     *            The request parameters.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @throws ServletException
     *             For any Servlet engine errors.
     * @throws IOException
     *             For any I/O errors.
     */
    @Inject
    public Including(Parameters parameters, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = "/included";
        if ("true".equals(parameters.get("qs"))) {
            path += "?a=1&b=2";
        }
        request.getRequestDispatcher(path).include(request, response);
    }
}
