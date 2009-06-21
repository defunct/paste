package com.goodworkalan.paste.forward;

import static com.goodworkalan.paste.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;
import static com.goodworkalan.paste.paths.FormatArguments.REQUEST_PATH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.paste.BasicScope;
import com.goodworkalan.paste.Janitor;
import com.goodworkalan.paste.PasteGuicer;
import com.goodworkalan.paste.PasteModule;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.SessionScope;
import com.goodworkalan.paste.ViewConnector;
import com.goodworkalan.paste.paths.FormatArgument;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ForwardTest
{
    @Test
    public void constructor()
    {
        ViewConnector viewBinder = mock(ViewConnector.class);
         
        new Forward(viewBinder);
    }
    
    @Test
    public void configure() throws IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        when(request.getContextPath()).thenReturn("");
        when(request.getParameterMap()).thenReturn(Collections.EMPTY_MAP);

        HttpServletResponse response = mock(HttpServletResponse.class);
        
        BasicScope requestScope = new BasicScope();
        BasicScope controllerScope = new BasicScope();
        PasteModule guicelet = new PasteModule(new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(guicelet);

        PasteGuicer.enterRequest(requestScope, request, response, "/account/create", Collections.<Janitor>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        PasteGuicer.enterController(controllerScope, injector, Object.class, new HashMap<String, String>());
        
        ViewConnector viewBinder = mock(ViewConnector.class);
        
        Forward forward = new Forward(viewBinder);
        
        Injector childInjector = injector.createChildInjector(forward);
        Renderer renderer = childInjector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = childInjector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "controller");
        assertEquals(configuration.getFormat(), "/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], CONTROLLER_CLASS_AS_PATH);
    }
    
    @Test
    public void property() throws IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        when(request.getContextPath()).thenReturn("");
        when(request.getParameterMap()).thenReturn(Collections.EMPTY_MAP);
        
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        BasicScope requestScope = new BasicScope();
        BasicScope controllerScope = new BasicScope();
        PasteModule guicelet = new PasteModule(new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(guicelet);

        PasteGuicer.enterRequest(requestScope, request, response, "/account/create", Collections.<Janitor>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        PasteGuicer.enterController(controllerScope, injector, Object.class, new HashMap<String, String>());
        
        ViewConnector viewBinder = mock(ViewConnector.class);
        
        Forward forward = new Forward(viewBinder);
        forward.property("property");
        
        Injector childInjector = injector.createChildInjector(forward);
        Renderer renderer = childInjector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = childInjector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "property");
        assertEquals(configuration.getFormat(), "/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], CONTROLLER_CLASS_AS_PATH);
    }
    
    @Test
    public void format() throws IOException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        when(request.getContextPath()).thenReturn("");
        when(request.getParameterMap()).thenReturn(Collections.EMPTY_MAP);

        HttpServletResponse response = mock(HttpServletResponse.class);
        
        BasicScope requestScope = new BasicScope();
        BasicScope controllerScope = new BasicScope();
        PasteModule guicelet = new PasteModule(new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(guicelet);

        PasteGuicer.enterRequest(requestScope, request, response, "/account/create", Collections.<Janitor>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        PasteGuicer.enterController(controllerScope, injector, Object.class, new HashMap<String, String>());
        
        ViewConnector viewBinder = mock(ViewConnector.class);
        
        Forward forward = new Forward(viewBinder);
        forward.format("/templates/%s.ftl", new FormatArgument[] { REQUEST_PATH });
        
        Injector childInjector = injector.createChildInjector(forward);
        Renderer renderer = childInjector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = childInjector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "controller");
        assertEquals(configuration.getFormat(), "/templates/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], REQUEST_PATH);
    }
}
