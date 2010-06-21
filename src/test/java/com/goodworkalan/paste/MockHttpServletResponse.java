package com.goodworkalan.paste;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

// TODO Document.
public class MockHttpServletResponse {
    // TODO Document.
    private final HttpServletResponse response;

    // TODO Document.
    private final StringWriter writer;

    // TODO Document.
    public MockHttpServletResponse() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        this.writer = new StringWriter();
        this.response = response;

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    // TODO Document.
    public HttpServletResponse getResponse() {
        return response;
    }
}
