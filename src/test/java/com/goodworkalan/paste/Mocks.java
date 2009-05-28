package com.goodworkalan.paste;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class Mocks
{
    public static HttpServletRequest request()
    {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeaderNames()).thenAnswer(new Answer<Enumeration<Object>>() {
            public Enumeration<Object> answer(InvocationOnMock invocation)
                    throws Throwable
            {
                return new Vector<Object>().elements();
            }
        });
        return request;
    }
    
    public static HttpServletResponse response()
    {
        HttpServletResponse response = mock(HttpServletResponse.class);
        return response;
    }
}
