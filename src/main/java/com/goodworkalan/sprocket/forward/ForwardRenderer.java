package com.goodworkalan.sprocket.forward;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.sprocket.Controller;
import com.goodworkalan.sprocket.Evaluator;
import com.goodworkalan.sprocket.Renderer;
import com.goodworkalan.sprocket.RequestScoped;
import com.goodworkalan.sprocket.paths.PathFormatter;
import com.google.inject.Inject;

// TODO Document.
@RequestScoped
public class ForwardRenderer implements Renderer
{
    // TODO Document.
    private final PathFormatter pathFormatter;
    
    // TODO Document.
    private final HttpServletRequest request;
    
    // TODO Document.
    private final HttpServletResponse response;
    
    // TODO Document.
    private final Object controller;
    
    // TODO Document.
    private final Configuration configuration;
    
    // TODO Document.
    @Inject
    public ForwardRenderer(
            PathFormatter pathFormatter,
            HttpServletRequest request,
            HttpServletResponse response,
            @Controller Object controller,
            Configuration configuration)
    {
        this.pathFormatter = pathFormatter;
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.configuration = configuration;
    }
    
    // TODO Document.
    public void render() throws ServletException, IOException
    {
        String path = pathFormatter.format(configuration.getFormat(), configuration.getFormatArguments());
        request.setAttribute(configuration.getProperty(), controller);
        request.setAttribute("evaluator", new Evaluator(controller));
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}
