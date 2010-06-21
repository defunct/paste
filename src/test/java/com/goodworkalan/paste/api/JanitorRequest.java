package com.goodworkalan.paste.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.controller.Janitor;
import com.goodworkalan.paste.controller.JanitorQueue;
import com.goodworkalan.paste.controller.qualifiers.Request;

// TODO Document.
public class JanitorRequest {
    // TODO Document.
    @Inject
    public JanitorRequest(@Request JanitorQueue janitors, final HttpSession session) {
        janitors.add(new Janitor() {
            public void cleanUp() {
                session.setAttribute("janitor", "request");
            }
        });
    }
}
