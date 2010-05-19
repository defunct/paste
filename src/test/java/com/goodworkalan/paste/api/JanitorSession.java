package com.goodworkalan.paste.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class JanitorSession {
    @Inject
    public JanitorSession(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.getWriter().println(session.getAttribute("janitor"));
    }
}
