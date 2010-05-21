package com.goodworkalan.paste.connector;


/**
 * Defines an applications routes, reactions, renderers and interceptors. This
 * interface is implemented by the application developer to wire up the
 * controllers in the application.
 * 
 * @author Alan Gutierrez
 */
public interface Router {
    /**
     * Construct an application by defining routes, reactions, renderers and
     * interceptors using the given connector.
     * 
     * @param connector The connector.
     */
    public void connect(Connector connector);
}
