package com.goodworkalan.paste.forward;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.RequestScoped;
import com.goodworkalan.paste.paths.PathFormatter;

/**
 * A renderer that renders the controller by forwarding to another filter or
 * servlet within the current web application.
 * 
 * @author Alan Gutierrez
 */
@RequestScoped
public class ForwardRenderer implements Renderer {
    /** A formatter that formats strings according to injected parameters. */
    private final PathFormatter pathFormatter;

    /** The request. */
    private final HttpServletRequest request;

    /** The response. */
    private final HttpServletResponse response;

    /** The controller. */
    private final Object controller;

    /** The forward rendering configuration. */
    private final Configuration configuration;

    /**
     * Construct a forward renderer.
     * 
     * @param pathFormatter
     *            A formatter that formats strings according to injected
     *            parameters.
     * @param request
     *            The request.
     * @param response
     *            The response.
     * @param controller
     *            The controller.
     * @param configuration
     *            The forward rendering configuration.
     */
    @Inject
    public ForwardRenderer(
            PathFormatter pathFormatter,
            HttpServletRequest request,
            HttpServletResponse response,
            @Controller Object controller,
            Configuration configuration) {
        this.pathFormatter = pathFormatter;
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.configuration = configuration;
    }

    /**
     * Render the configuration by forwarding the request within the web
     * application.
     */
    public void render() throws ServletException, IOException {
        String path = pathFormatter.format(configuration.format,  configuration.formatArguments);
        request.setAttribute(configuration.property, controller);
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}
