package com.goodworkalan.guicelet.forward;

import com.goodworkalan.guicelet.RenderModule;
import com.goodworkalan.guicelet.ViewBinder;

public class Forward extends RenderModule
{
    private String format = "%s.ftl";
    
    private String directory = "/";

    public Forward(ViewBinder viewBinder)
    {
        super(viewBinder);
    }
    
    @Override
    protected void configure()
    {
        bind(String.class).annotatedWith(Format.class).toInstance(format);
        bind(String.class).annotatedWith(Directory.class).toInstance(directory);
    }
    
    public Forward directory(String directory)
    {
        this.directory = directory;
        return this;
    }
    
    public Forward format(String format)
    {
        this.format = format;
        return this;
    }
}
