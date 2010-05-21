package com.goodworkalan.paste.controller;

public class Stop extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public Stop() {
    }

    public Stop(Throwable cause) {
        super(cause);
    }
}
