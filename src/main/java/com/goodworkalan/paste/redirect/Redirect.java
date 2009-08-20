package com.goodworkalan.paste.redirect;

import static com.goodworkalan.paste.redirect.Redirects.isRedirectStatus;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.RenderModule;
import com.goodworkalan.paste.Renderer;
import com.google.inject.Provider;

/**
 * An extension element in a domain-specific language use to specify the 
 * details of an HTTP redirection. 
 *
 * @author Alan Gutierrez
 */
public class Redirect extends RenderModule
{
    /** The status code to set during the redirection. */
    private int status = 303;

    /** The format to use to create the redirection URL. */
    private String format;
    
    /** The format arguments to use to create the redirection URL. */
    private Class<?>[] formatArguments = new Class<?>[0];

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of an HTTP redirection.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Redirect(Connector end)
    {
        super(end);
    }

    /**
     * Configure the a Guice child injector to include the properties necessary
     * to create a {@link Renderer} that will generate an HTTP redirection.
     */
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

    /**
     * Set the status code to set during the redirection.
     * 
     * @param status
     *            The status code to set during the redirection.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Redirect status(int status)
    {
        if (!isRedirectStatus(status))
        {
            throw new IllegalArgumentException();
        }
        this.status = status;
        return this;
    }

    /**
     * Set the format string and the format arguments used to create the
     * redirection URL. Format arguments are objects that generate argument
     * values using the Guice injector, to replace format parameters with values
     * from the servlet environment.
     * 
     * @param format
     *            The format string.
     * @param formatArguments
     *            The format arguments.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Redirect format(String format, Class<?>...formatArguments)
    {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
