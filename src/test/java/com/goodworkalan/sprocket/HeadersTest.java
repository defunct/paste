package com.goodworkalan.sprocket;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.goodworkalan.sprocket.Headers;

public class HeadersTest
{
    @Test
    public void methodConstructor()
    {
        Headers headers = new Headers("GET");
        assertEquals(headers.getMethod(), "GET");
    }
}
