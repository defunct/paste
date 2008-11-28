package com.goodworkalan.guicelet.forward;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Renderer;
import com.google.inject.Inject;

public class ForwardRenderer implements Renderer
{
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private final Object controller;
    
    private final String directory;
    
    private final String format;
    
    private final String property;
    
    @Inject
    public ForwardRenderer(
            HttpServletRequest request,
            HttpServletResponse response,
            @Controller Object controller,
            @Directory String directory,
            @Format String format,
            @Property String property)
    {
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.directory = directory;
        this.format = format;
        this.property = property;
    }
    
    public void render(Class<? extends Annotation> bundle, String name)
        throws ServletException, IOException
    {
        String path = directory + String.format(format, name);
        request.setAttribute(property, controller);
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
}
