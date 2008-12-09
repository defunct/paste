package com.goodworkalan.guicelet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HeadersTest
{
    @Test
    public void methodConstructor()
    {
        Headers headers = new Headers("GET");
        assertEquals(headers.getMethod(), "GET");
    }
}
