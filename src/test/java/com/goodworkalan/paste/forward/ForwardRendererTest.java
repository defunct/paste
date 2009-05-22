package com.goodworkalan.paste.forward;

import static com.goodworkalan.paste.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.forward.Configuration;
import com.goodworkalan.paste.forward.ForwardRenderer;
import com.goodworkalan.paste.paths.FormatArgument;
import com.goodworkalan.paste.paths.PathFormatter;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ForwardRendererTest
{
    @Test public void forward() throws ServletException, IOException
    {
        final Object controller = new Object();
        
        Injector injector = Guice.createInjector(new AbstractModule()
        {
            @Override
            protected void configure()
            {
                bind(Object.class).annotatedWith(Controller.class).toInstance(controller);
            }
        });

        PathFormatter formatter = new PathFormatter(injector);

        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestDispatcher("/java/lang/Object.ftl")).thenReturn(dispatcher);
        
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        Configuration configuration = new Configuration("controller", "/%s.ftl", new FormatArgument[] { CONTROLLER_CLASS_AS_PATH });
        
        Renderer renderer = new ForwardRenderer(formatter, request, response, controller, configuration);
        renderer.render();
        
        verify(request).setAttribute("controller", controller);
        verify(dispatcher).forward(request, response);
    }
}
