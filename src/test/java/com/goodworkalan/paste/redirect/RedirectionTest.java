package com.goodworkalan.paste.redirect;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.Test;

import com.goodworkalan.paste.MockHttpServletRequest;

public class RedirectionTest
{
    @Test
    public void constructor()
    {
        MockHttpServletRequest request = new MockHttpServletRequest();
        when(request.getRequest().getRequestURL()).thenReturn(new StringBuffer("http://domain.com/foo/baz"));
        
        final Map<String, String> headers = new HashMap<String, String>();
        HttpServletResponse response = mock(HttpServletResponse.class);
        Mockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) throws Throwable {
                headers.put((String) invocation.getArguments()[0], (String) invocation.getArguments()[1]);
                return null;
            }
        }).when(response).addHeader(Mockito.anyString(), Mockito.anyString());

        Redirector redirector = new Redirector(request.getRequest(), response);
        
        Redirection redirection = new Redirection("bar");
        redirector.redirect(redirection.getWhere());
        
        assertEquals(headers.get("Location"), "http://domain.com/foo/bar");   
    }
}
