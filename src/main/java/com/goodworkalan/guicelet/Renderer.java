package com.goodworkalan.guicelet;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.ServletException;

public interface Renderer
{
    public void render(Class<? extends Annotation> bundle, String name) throws ServletException, IOException;
}
