package com.goodworkalan.guicelet.redirect;

import static com.goodworkalan.guicelet.redirect.Redirects.isRedirectStatus;

import com.goodworkalan.guicelet.RenderModule;
import com.goodworkalan.guicelet.Renderer;
import com.goodworkalan.guicelet.ViewBinder;
import com.goodworkalan.guicelet.paths.FormatArgument;
import com.google.inject.Provider;

// TODO Document.
public class Redirect extends RenderModule
{
    // TODO Document.
    private int status = 303;

    // TODO Document.
    private String format;
    
    // TODO Document.
    private FormatArgument[] formatArguments = new FormatArgument[0];
    
    // TODO Document.
    public Redirect(ViewBinder binder)
    {
        super(binder);
    }

    // TODO Document.
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
    
    // TODO Document.
    public Redirect status(int status)
    {
        if (!isRedirectStatus(status))
        {
            throw new IllegalArgumentException();
        }
        this.status = status;
        return this;
    }
    
    // TODO Document.
    public Redirect format(String format, FormatArgument...formatArguments)
    {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
