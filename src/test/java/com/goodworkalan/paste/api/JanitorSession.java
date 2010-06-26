package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A controller to inspect the "janitor" attribute of the HTTP session which
 * is used by the <code>Janitor</code> test controllers to indicate whether
 * a request or filtration jaintor has been invoked.
 *
 * @author Alan Gutierrez
 */
public class JanitorSession {
    /**
     * Print the contents of the "janitor" attribute of the HTTP session.
     * 
     * @param request
     *            The HTTP request.
     * @param session
     *            The HTTP session.
     * @param response
     *            The HTTP response.
     * @throws IOException
     *             For any I/O error.
     */
    @Inject
    public JanitorSession(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(session.getAttribute("janitor"));
    }
}
