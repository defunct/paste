package com.goodworkalan.paste;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;

import org.testng.annotations.Test;

import com.goodworkalan.paste.ResponseHeaders;

public class HeadersTest
{
    @Test
    public void methodConstructor()
    {
        ResponseHeaders headers = new ResponseHeaders(new ArrayList<NamedValue>());
        assertEquals(headers.getStatus(), 0);
    }
}
