package com.goodworkalan.guicelet.forward;

import com.goodworkalan.guicelet.RenderModule;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.ViewBinder;

public class Forward extends RenderModule
{
    private String format = "%s.ftl";
    
    private String directory = "/";
    
    private String property = "controller";

    public Forward(ViewBinder viewBinder)
    {
        super(viewBinder);
    }
    
    @Override
    protected void configure()
    {
        bind(Renderer.class).to(ForwardRenderer.class);
        bind(String.class).annotatedWith(Format.class).toInstance(format);
        bind(String.class).annotatedWith(Directory.class).toInstance(directory);
        bindConstant().annotatedWith(Property.class).to(property);
    }
    
    public Forward directory(String directory)
    {
        this.directory = directory;
        return this;
    }
    
    public Forward property(String property)
    {
        this.property = property;
        return this;
    }
    
    public Forward format(String format)
    {
        this.format = format;
        return this;
    }
}
