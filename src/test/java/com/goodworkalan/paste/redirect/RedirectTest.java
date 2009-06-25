package com.goodworkalan.paste.redirect;

import static com.goodworkalan.paste.paths.FormatArguments.REQUEST_DIRECTORY_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.dovetail.Glob;
import com.goodworkalan.paste.BasicScope;
import com.goodworkalan.paste.PasteModule;
import com.goodworkalan.paste.Routes;
import com.goodworkalan.paste.ScopeManager;
import com.goodworkalan.paste.SessionScope;
import com.goodworkalan.paste.ViewConnector;
import com.goodworkalan.paste.janitor.Janitor;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class RedirectTest
{
    @Test
    public void createInjector() throws IOException
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
        PasteModule guicelet = new PasteModule(new Routes(Collections.<Class<?>, Glob>emptyMap()), new SessionScope(), requestScope, controllerScope, Collections.<Janitor>emptyList());
        Injector injector = Guice.createInjector(guicelet);

        ScopeManager scopeManager = new ScopeManager(requestScope, request, response, "/account/create", Collections.<Janitor>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        scopeManager.enterRequest();
        scopeManager.enterController(controllerScope, injector, Object.class, new HashMap<String, String>());
        
        ViewConnector binder = mock(ViewConnector.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(301);
        redirect.format("%s/index", REQUEST_DIRECTORY_NAME);
   
        injector.createChildInjector(redirect).getInstance(Configuration.class);
    }
    
    @Test(expectedExceptions=IllegalArgumentException.class)
    public void isRedirectStatus()
    {
        ViewConnector binder = mock(ViewConnector.class);
        
        Redirect redirect = new Redirect(binder);
        redirect.status(200);
    }
}
