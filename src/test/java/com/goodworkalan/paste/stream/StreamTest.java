package com.goodworkalan.paste.stream;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.goodworkalan.paste.BasicScope;
import com.goodworkalan.paste.PasteGuicer;
import com.goodworkalan.paste.PasteModule;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.SessionScope;
import com.goodworkalan.paste.ViewConnector;
import com.goodworkalan.paste.janitor.Janitor;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StreamTest
{
    @Test
    public void stream() throws IOException, ServletException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/account/create");
        
        when(request.getParameterMap()).thenReturn(Collections.EMPTY_MAP);
        
        final Map<String, String> headers = new HashMap<String, String>();
        StringWriter writer = new StringWriter();
        HttpServletResponse response = mock(HttpServletResponse.class);
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(response.getOutputStream()).thenReturn(mock(ServletOutputStream.class));
        doAnswer(new Answer<Object>()
        { 
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                headers.put((String) args[0], (String) args[1]);
                return null;
            }
        }).when(response).addHeader(anyString(), anyString());
        
        BasicScope requestScope = new BasicScope();
        BasicScope controllerScope = new BasicScope();
        PasteModule paste = new PasteModule(new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(paste);

        PasteGuicer.enterRequest(requestScope, request, response, "/account/create", Collections.<Janitor>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        PasteGuicer.enterController(controllerScope, injector, StreamController.class, new HashMap<String, String>());
        
        ViewConnector binder = mock(ViewConnector.class);
        
        Stream stream = new Stream(binder);
        stream.contentType("text/csv");
   
        Renderer renderer = injector.createChildInjector(stream).getInstance(Renderer.class);
        renderer.render();

        assertEquals("a,b,c\r\n", writer.toString());
    }
}
