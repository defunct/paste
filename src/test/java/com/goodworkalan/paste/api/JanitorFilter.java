package com.goodworkalan.paste.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.controller.JanitorQueue;

/**
 * A controller to test <code>Janitor</code> cleanup during a forward or include
 * filtration.
 * 
 * @author Alan Gutierrez
 */
public class JanitorFilter {
    /**
     * Set a session parameter during filter <code>Janitor.cleanUp</code>.
     * 
     * @param janitors
     *            The filter queue of janitors.
     * @param session
     *            The HTTP session.
     */
    @Inject
    public JanitorFilter(JanitorQueue janitors, final HttpSession session) {
        janitors.add(new Runnable() {
            public void run() {
                session.setAttribute("janitor", "filter");
            }
        });
    }
}
