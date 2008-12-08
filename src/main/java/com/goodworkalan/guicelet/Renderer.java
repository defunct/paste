package com.goodworkalan.guicelet;

import java.io.IOException;

import javax.servlet.ServletException;

public interface Renderer
{
    public void render() throws ServletException, IOException;
}
