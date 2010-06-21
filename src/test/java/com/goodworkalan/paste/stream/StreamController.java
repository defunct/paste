package com.goodworkalan.paste.stream;

// TODO Document.
public class StreamController {
    // TODO Document.
    @Output(contentType = "text/csv")
    public String csv() {
        return "a,b,c\r\n";
    }
}
