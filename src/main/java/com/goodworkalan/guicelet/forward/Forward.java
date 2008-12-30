package com.goodworkalan.guicelet.forward;

import static com.goodworkalan.guicelet.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;

import com.goodworkalan.guicelet.RenderModule;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.ViewBinder;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.google.inject.Provider;

public class Forward extends RenderModule
{
    private String format = "/%s.ftl";
    
    private String property = "controller";
    
    private FormatArgument[] formatArguments = new FormatArgument[]
    {
            CONTROLLER_CLASS_AS_PATH
    };

    public Forward(ViewBinder viewBinder)
    {
        super(viewBinder);
    }
    
    @Override
    protected void configure()
    {
        bind(Renderer.class).to(ForwardRenderer.class);
        bind(Configuration.class).toProvider(new Provider<Configuration>()
        {
            public Configuration get()
            {
                return new Configuration(property, format, formatArguments);
            }
        });
    }
    
    public Forward property(String property)
    {
        this.property = property;
        return this;
    }
    
    public Forward format(String format, FormatArgument...formatArguments)
    {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
