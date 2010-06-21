package com.goodworkalan.paste;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO Document.
public class MockHttpServletRequest {
    // TODO Document.
    private final HttpServletRequest request;

    // TODO Document.
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

    // TODO Document.
    public HttpServletRequest getRequest() {
        return request;
    }
}
