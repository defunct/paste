package com.goodworkalan.paste;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockHttpServletRequest {
    private final HttpServletRequest request;

    public MockHttpServletRequest() {
      HttpServletRequest request = mock(HttpServletRequest.class);
       when(request.getHeaderNames()).thenAnswer(new Answer<Enumeration<Object>>() {
           public Enumeration<Object> answer(InvocationOnMock invocation)
           throws Throwable {
               return new Vector<Object>().elements();
           }
       });
       this.request = request;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
}
