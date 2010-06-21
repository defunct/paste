package com.goodworkalan.paste.forward;

import java.io.IOException;

import javax.servlet.ServletException;

import org.testng.annotations.Test;

// TODO Document.
public class ForwardRendererTest {
    // TODO Document.
    @Test
    public void forward() throws ServletException, IOException {
//        final Object controller = new Object();
//        
//        Injector injector = Guice.createInjector(new AbstractModule()
//        {
//            @Override
//            protected void configure()
//            {
//                bind(Object.class).annotatedWith(Controller.class).toInstance(controller);
//            }
//        });
//
//        PathFormatter formatter = new PathFormatter(injector);
//
//        RequestDispatcher dispatcher = mock(RequestDispatcher.class);
//        
//        HttpServletRequest request = mock(HttpServletRequest.class);
//        when(request.getRequestDispatcher("/java/lang/Object.ftl")).thenReturn(dispatcher);
//        
//        HttpServletResponse response = mock(HttpServletResponse.class);
//        
//        Configuration configuration = new Configuration("controller", "/%s.ftl", new Class<?>[] { ControllerClassAsPath.class });
//        
//        Renderer renderer = new ForwardRenderer(formatter, request, response, controller, configuration);
//        renderer.render();
//        
//        verify(request).setAttribute("controller", controller);
//        verify(dispatcher).forward(request, response);
    }
}
