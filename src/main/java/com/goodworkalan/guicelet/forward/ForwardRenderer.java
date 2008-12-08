package com.goodworkalan.guicelet.forward;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.RequestScoped;
import com.goodworkalan.guicelet.paths.PathFormatter;
import com.google.inject.Inject;

@RequestScoped
public class ForwardRenderer implements Renderer
{
    private final PathFormatter pathFormatter;
    
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private final Object controller;
    
    private final Configuration configuration;
    
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
    
    public void render() throws ServletException, IOException
    {
        String path = pathFormatter.format(configuration.getFormat(), configuration.getFormatArguments());
        request.setAttribute(configuration.getProperty(), controller);
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}
