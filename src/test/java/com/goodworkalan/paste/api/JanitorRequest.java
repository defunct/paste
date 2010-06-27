package com.goodworkalan.paste.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.controller.JanitorQueue;
import com.goodworkalan.paste.controller.qualifiers.Request;

/**
 * A controller to test <code>Janitor</code> cleanup during a request.
 * 
 * @author Alan Gutierrez
 */
public class JanitorRequest {
    /**
     * Set a session parameter during request <code>Janitor.cleanUp</code>.
     * 
     * @param janitors
     *            The request queue of janitors.
     * @param session
     *            The HTTP session.
     */
    @Inject
    public JanitorRequest(@Request JanitorQueue janitors, final HttpSession session) {
        janitors.add(new Runnable() {
            public void run() {
                session.setAttribute("janitor", "request");
            }
        });
    }
}
