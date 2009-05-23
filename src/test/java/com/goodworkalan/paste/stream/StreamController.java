package com.goodworkalan.paste.stream;

public class StreamController
{
    @Output(contentType = "text/csv")
    public String csv() 
    {
        return "a,b,c\r\n";
    }
}
