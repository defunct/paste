package com.goodworkalan.minimal.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.goodworkalan.minimal.persistence.Migration;

/**
 * Application context listener that performs any necessary database migrations
 * prior to initializing the application.
 * 
 * @author Alan Gutierrez
 */
public class MinimalServletContextListener implements ServletContextListener {
    /**
     * Initialize the servlet context by performing any database migrations.
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            Migration.migrate();
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    /**
     * Nothing to do when the Servlet context is destroyed.
     */
    public void contextDestroyed(ServletContextEvent event) {
    }
}