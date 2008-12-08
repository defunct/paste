package com.goodworkalan.guicelet.redirect;

import com.goodworkalan.guicelet.RenderModule;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.ViewBinder;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.google.inject.Provider;

public class Redirect extends RenderModule
{
    private int status = 303;

    private String format;
    
    private FormatArgument[] formatArguments = new FormatArgument[0];
    
    public Redirect(ViewBinder viewBinder)
    {
        super(viewBinder);
    }

    @Override
    protected void configure()
    {
        bind(Renderer.class).to(RedirectRenderer.class);
        bind(Configuration.class).toProvider(new Provider<Configuration>()
        {
            public Configuration get()
            {
                return new Configuration(status, format, formatArguments);
            }
        });
    }
    
    public Redirect status(int status)
    {
        switch (status)
        {
        case 300:
        case 301:
        case 302:
        case 303:
        case 307:
            break;
        default:
            throw new IllegalStateException();
        }
        this.status = status;
        return this;
    }
    
    public Redirect format(String format, FormatArgument[] formatArguments)
    {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
