package com.goodworkalan.paste.controller;

import java.io.IOException;

import javax.servlet.ServletException;

/**
 * Generates output to return to the client of an HTTP request. Implementations
 * are constructed using dependency injection.
 * 
 * @author Alan Gutierrez
 */
public interface Renderer {
    /**
     *Generate output to return the client of the HTTP request
     * 
     * @throws IOException
     *             For any I/O error.
     * @throws ServletException
     *             For any difficulty encountered by the servlet API
     *             implementation.
     */
    public void render() throws ServletException, IOException;
}
