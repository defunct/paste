package com.goodworkalan.guicelet.redirect;

import static org.mockito.Mockito.mock;

import javax.servlet.http.HttpServletRequest;

import org.testng.annotations.Test;

import com.goodworkalan.guicelet.Headers;

public class RedirectorTest
{
    @Test
    public void constructor()  
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Headers headers = new Headers("GET");
        new Redirector(request, headers);
    }
}
