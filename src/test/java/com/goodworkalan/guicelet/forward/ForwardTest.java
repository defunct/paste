package com.goodworkalan.guicelet.forward;

import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;
import static com.goodworkalan.guicelet.paths.FormatArguments.REQUEST_PATH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.GuiceletModule;
import com.goodworkalan.guicelet.Janitor;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.ViewBinder;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ForwardTest
{
    @Test
    public void constructor()
    {
        ViewBinder viewBinder = mock(ViewBinder.class);
         
        new Forward(viewBinder);
    }
    
    @Test
    public void configure()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        
        HttpServletResponse repsonse = mock(HttpServletResponse.class);
        Object controller = new Object();
        
        GuiceletModule guiceletModule = new GuiceletModule(
                request,
                repsonse,
                new ArrayList<Janitor>(),
                controller,
                new Parameters());
        
        ViewBinder viewBinder = mock(ViewBinder.class);
        
        Forward forward = new Forward(viewBinder);
        Injector injector = Guice.createInjector(guiceletModule, forward);
        
        Renderer renderer = injector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = injector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "controller");
        assertEquals(configuration.getFormat(), "/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], CONTROLLER_CLASS_AS_PATH);
    }
    
    @Test
    public void property()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        
        HttpServletResponse repsonse = mock(HttpServletResponse.class);
        Object controller = new Object();
        
        GuiceletModule guiceletModule = new GuiceletModule(
                request,
                repsonse,
                new ArrayList<Janitor>(),
                controller,
                new Parameters());
        
        ViewBinder viewBinder = mock(ViewBinder.class);
        
        Forward forward = new Forward(viewBinder);
        forward.property("property");
        Injector injector = Guice.createInjector(guiceletModule, forward);
        
        Renderer renderer = injector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = injector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "property");
        assertEquals(configuration.getFormat(), "/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], CONTROLLER_CLASS_AS_PATH);
    }
    
    @Test
    public void format()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        
        HttpServletResponse repsonse = mock(HttpServletResponse.class);
        Object controller = new Object();
        
        GuiceletModule guiceletModule = new GuiceletModule(
                    request,
                    repsonse,
                    new ArrayList<Janitor>(),
                    controller,
                    new Parameters());
        
        ViewBinder viewBinder = mock(ViewBinder.class);
        
        Forward forward = new Forward(viewBinder);
        forward.format("/templates/%s.ftl", new FormatArgument[] { REQUEST_PATH });
        Injector injector = Guice.createInjector(guiceletModule, forward);
        
        Renderer renderer = injector.getInstance(Renderer.class);
        assertTrue(renderer instanceof ForwardRenderer);
        
        Configuration configuration = injector.getInstance(Configuration.class);
        assertEquals(configuration.getProperty(), "controller");
        assertEquals(configuration.getFormat(), "/templates/%s.ftl");
        assertSame(configuration.getFormatArguments()[0], REQUEST_PATH);
    }
}
