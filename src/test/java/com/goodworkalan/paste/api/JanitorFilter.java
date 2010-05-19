package com.goodworkalan.paste.api;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.goodworkalan.paste.janitor.Janitor;
import com.goodworkalan.paste.janitor.JanitorQueue;

public class JanitorFilter {
    @Inject
    public JanitorFilter(JanitorQueue janitors, final HttpSession session) {
        janitors.add(new Janitor() {
            public void cleanUp() {
                session.setAttribute("janitor", "filter");
            }
        });
    }
}
