package com.goodworkalan.paste.connector;

import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.redirect.Redirect;

/**
 * Define the default handling for redirecitons.
 * 
 * @author Alan Gutierrez
 */
public class RedirectionRouter implements Router {
    /**
     * Connection the <code>Redirection</code> exception to the
     * <code>Redirect</code> renderer.
     * 
     * @param connector
     *            The connector.
     */
    public void connect(Connector connector) {
        connector
            .render()
                .exception(Redirection.class)
                .with(Redirect.class)
                .end()
            .end();
    }
}
