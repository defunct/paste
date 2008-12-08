package com.goodworkalan.guicelet.forward;

import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.Transfer;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.goodworkalan.guicelet.paths.PathFormatter;

public class ForwardRendererTest
{
    @Test public void forward() throws ServletException, IOException
    {
        Object controller = new Object();

        Transfer transfer = mock(Transfer.class);
        when(transfer.getController()).thenReturn(controller);

        PathFormatter formatter = new PathFormatter(transfer);

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
