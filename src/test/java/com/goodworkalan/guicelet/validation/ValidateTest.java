package com.goodworkalan.guicelet.validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.guicelet.BindingController;
import com.goodworkalan.guicelet.CoreBinder;
import com.goodworkalan.guicelet.GuiceletGuicer;
import com.google.inject.Guice;

public class ValidateTest
{
    public void test() throws IOException, ServletException
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
        GuiceletGuicer guicer = binder.newGuiceletGuicer(Guice.createInjector());
        guicer.filter(request, response, chain);
    }
}
