package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A test Servlet that writes a simple greeting in order to test interception.
 * 
 * @author Alan Gutierrez
 */
public class TestServlet extends HttpServlet {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    /**
     * Write a greeting to output.
     * 
     * @param req
     *            The HTTP request.
     * @param resp
     *            The HTTP response.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Hello, World!\n");
    }
}
