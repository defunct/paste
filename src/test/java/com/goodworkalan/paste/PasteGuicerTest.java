package com.goodworkalan.paste;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.testng.annotations.Test;

import com.goodworkalan.paste.CoreBinder;
import com.goodworkalan.paste.PasteGuicer;
import com.goodworkalan.paste.forward.Forward;
import com.google.inject.Module;

// FIXME Rename.
public class PasteGuicerTest
{
    @Test
    public void constructor() throws IOException, ServletException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/servlet/queue/alan");
        when(request.getContextPath()).thenReturn("/servlet");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));

        HttpServletResponse response = mock(HttpServletResponse.class);
        
        FilterChain chain = mock(FilterChain.class);
        
        CoreBinder binder = new CoreBinder();
        binder.controllers(Object.class)
              .bind("/queue/{user}")
              .to(BindingController.class);
        PasteGuicer guicer = binder.newGuiceletGuicer(Collections.<Module>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        guicer.filter(request, response, chain);
    }
    
    @Test
    public void method() throws IOException, ServletException
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/servlet/login");
        when(request.getContextPath()).thenReturn("/servlet");
        when(request.getMethod()).thenReturn("GET");
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(Collections.emptyList()));

        HttpServletResponse response = mock(HttpServletResponse.class);
        
        FilterChain chain = mock(FilterChain.class);
        
        CoreBinder binder = new CoreBinder();
        binder
            .controllers(Object.class)
                  .bind("/queue/{user}").to(BindingController.class)
                  .bind("/login")
                      .when().method("GET").priority(1).to(Object.class)
                      .when().method("POST").to(Object.class)
                  .bind("/index").to(Object.class);
        
        PasteGuicer guicer = binder.newGuiceletGuicer(Collections.<Module>emptyList(), mock(ServletContext.class), Collections.<String, String>emptyMap());
        guicer.filter(request, response, chain);
    }
    
    @Test
    public void view() throws IOException, ServletException
    {
        CoreBinder binder = new CoreBinder();
        binder
            .controllers(Object.class)
                  .bind("/queue/{user}").to(BindingController.class)
                  .bind("/login")
                      .when().method("GET").priority(1).to(Object.class)
                      .when().method("POST").to(Object.class)
                  .bind("/index").to(Object.class);
        
        binder
            .view()
                .controller(Object.class).or(Index.class)
                .view()
                    .method("GET").or().exception(Error.class).with(Forward.class).format("/foo")
                    .end()
                .view()
                    .method("POST").with(Forward.class).format("/foo")
                    .end()
                .end();
    }
}
