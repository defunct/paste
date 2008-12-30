package com.goodworkalan.guicelet.redirect;

import static com.goodworkalan.guicelet.paths.FormatArguments.REQUEST_DIRECTORY_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.ControllerModule;
import com.goodworkalan.guicelet.GuiceletModule;
import com.goodworkalan.guicelet.Janitor;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.ViewBinder;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class RedirectTest
{
    @Test
    public void createInjector()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));
        when(request.getRequestURI()).thenReturn("/account/create");
        
        HttpServletResponse repsonse = mock(HttpServletResponse.class);
        
        GuiceletModule guicelet = new GuiceletModule(
                request,
                repsonse,
                new HashMap<Class<? extends Annotation>, List<Janitor>>(),
                new Parameters());
        ControllerModule controller = new ControllerModule(new Object());

        ViewBinder binder = mock(ViewBinder.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(301);
        redirect.format("%s/index", REQUEST_DIRECTORY_NAME);
   
        Injector injector = Guice.createInjector(guicelet, controller, redirect);
        injector.getInstance(Configuration.class);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void isRedirectStatus()
    {
        ViewBinder binder = mock(ViewBinder.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(200);
    }
}
