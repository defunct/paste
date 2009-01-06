package com.goodworkalan.guicelet.redirect;

import static com.goodworkalan.guicelet.paths.FormatArguments.REQUEST_DIRECTORY_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.BasicScope;
import com.goodworkalan.guicelet.GuiceletModule;
import com.goodworkalan.guicelet.Janitor;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.Scopes;
import com.goodworkalan.guicelet.SessionScope;
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
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/account/create");
        
        when(request.getParameterMap()).thenReturn(Collections.EMPTY_MAP);
        
        HttpServletResponse response = mock(HttpServletResponse.class);
        
        BasicScope requestScope = new BasicScope();
        BasicScope controllerScope = new BasicScope();
        GuiceletModule guicelet = new GuiceletModule(new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(guicelet);

        Scopes.enterRequest(requestScope, request, response, Collections.<Janitor>emptyList());
        Scopes.enterController(controllerScope, injector, Object.class, new Parameters());
        
        ViewBinder binder = mock(ViewBinder.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(301);
        redirect.format("%s/index", REQUEST_DIRECTORY_NAME);
   
        injector.createChildInjector(redirect).getInstance(Configuration.class);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void isRedirectStatus()
    {
        ViewBinder binder = mock(ViewBinder.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(200);
    }
}
