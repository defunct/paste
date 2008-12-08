package com.goodworkalan.guicelet.forward;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Path;
import com.goodworkalan.guicelet.Paths;
import com.goodworkalan.guicelet.Renderer;
import com.google.inject.Inject;

public class ForwardRenderer implements Renderer
{
    private final HttpServletRequest request;
    
    private final HttpServletResponse response;
    
    private final String path;
    
    private final Object controller;
    
    private final String format;
    
    private final Paths[] arguments;
    
    private final String property;
    
    @Inject
    public ForwardRenderer(
            HttpServletRequest request,
            HttpServletResponse response,
            @Path String path,
            @Controller Object controller,
            @Format String format,
            @FormatParameters Paths[] arguments,
            @Property String property)
    {
        this.request = request;
        this.response = response;
        this.controller = controller;
        this.path = path;
        this.format = format;
        this.property = property;
        this.arguments = arguments;
    }
    
    public void render() throws ServletException, IOException
    {
        String path = String.format(format, getFormatArguments());
        request.setAttribute(property, controller);
        RequestDispatcher dispatcher = request.getRequestDispatcher(path);
        dispatcher.forward(request, response);
    }
    
    private Object[] getFormatArguments()
    {
        Object[] arguments = new String[this.arguments.length];
        for (int i = 0; i < arguments.length; i++)
        {
            arguments[i] = getArgument(this.arguments[i]);
        }
        return arguments;
    }
    
    public String getArgument(Paths paths)
    {
        switch (paths)
        {
        case REQUEST_PATH:
            return path;
        case REQUEST_DIRECTORY_NAME:
            return path.substring(0, path.lastIndexOf('/') + 1);
        case REQUEST_FILE_NAME:
            int toothpick = path.lastIndexOf('/');
            if (toothpick != -1 && toothpick + 1 < path.length())
            {
                return path.substring(toothpick + 1);
            }
            return "index";
        case CONTROLLER_PATH:
            return controller.getClass().getCanonicalName().replace('.', '/').replace('$', '/');
        case CONTROLLER_PACKAGE_NAME:
            return controller.getClass().getPackage().getName().replace('.', '/');
        case CONTROLLER_CLASS_NAME:
            String name = controller.getClass().getCanonicalName();
            String pkg = controller.getClass().getPackage().getName();
            return name.substring(pkg.length()).replace('.', '/').replace('$', '/');
        default:
            throw new IllegalArgumentException();
        }
    }
}
