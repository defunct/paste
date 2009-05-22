package com.goodworkalan.paste.forward;

import static com.goodworkalan.paste.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;

import com.goodworkalan.paste.ControllerScoped;
import com.goodworkalan.paste.RenderModule;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.ViewBinder;
import com.goodworkalan.paste.paths.FormatArgument;
import com.google.inject.Provider;

// TODO Document.
public class Forward extends RenderModule
{
    // TODO Document.
    private String format = "/%s.ftl";
    
    // TODO Document.
    private String property = "controller";
    
    // TODO Document.
    private FormatArgument[] formatArguments = new FormatArgument[]
    {
            CONTROLLER_CLASS_AS_PATH
    };

    // TODO Document.
    public Forward(ViewBinder viewBinder)
    {
        super(viewBinder);
    }
    
    // TODO Document.
    @Override
    protected void configure()
    {
        bind(Renderer.class).to(ForwardRenderer.class).in(ControllerScoped.class);
        bind(Configuration.class).toProvider(new Provider<Configuration>()
        {
            public Configuration get()
            {
                return new Configuration(property, format, formatArguments);
            }
        }).in(ControllerScoped.class);
    }
    
    // TODO Document.
    public Forward property(String property)
    {
        this.property = property;
        return this;
    }
    
    // TODO Document.
    public Forward format(String format, FormatArgument...formatArguments)
    {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
