package com.goodworkalan.guicelet.forward;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import com.goodworkalan.guicelet.Controller;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.Transfer;
import com.google.inject.Inject;

public class ForwardRenderer implements Renderer
{
    private final Transfer transfer;
    
    private final Object controller;
    
    private final Configuration configuration;
    
    @Inject
    public ForwardRenderer(
            Transfer transfer,
            @Controller Object controller,
            Configuration configuration)
    {
        this.transfer = transfer;
        this.controller = controller;
        this.configuration = configuration;
    }
    
    public void render() throws ServletException, IOException
    {
        Object[] args = new String[configuration.getFormatArguments().length];
        for (int i = 0; i < args.length; i++)
        {
            args[i] = configuration.getFormatArguments()[i].getArgument(transfer);
        }
        String path = String.format(configuration.getFormat(), args);
        transfer.getHttpServletRequest().setAttribute(configuration.getProperty(), controller);
        RequestDispatcher dispatcher = transfer.getHttpServletRequest().getRequestDispatcher(path);
        dispatcher.forward(transfer.getHttpServletRequest(), transfer.getHttpServletResponse());
    }
}
