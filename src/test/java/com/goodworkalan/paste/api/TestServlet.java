package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// TODO Document.
public class TestServlet extends HttpServlet {
    /** Serial version id. */
    private static final long serialVersionUID = 1L;

    // TODO Document.
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Hello, World!\n");
    }
}
