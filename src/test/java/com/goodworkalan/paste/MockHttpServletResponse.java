package com.goodworkalan.paste;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

public class MockHttpServletResponse {
    private final HttpServletResponse response;

    private final StringWriter writer;

    public MockHttpServletResponse() throws IOException {
        HttpServletResponse response = mock(HttpServletResponse.class);

        this.writer = new StringWriter();
        this.response = response;

        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
