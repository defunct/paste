package com.goodworkalan.paste.stream;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.RenderModule;
import com.goodworkalan.paste.Renderer;
import com.google.inject.Provider;

/**
 * An extension element in the domain-specific language that is used to specify
 * a stream rendering response, a response that is the stream output of a method
 * in a controller.
 * 
 * @author Alan Gutierrez
 */
public class Stream extends RenderModule
{
    /** The method name to call or null if we match on mime-type. */
    private String methodName;
    
    /** The mime-type to send. */
    private String contentType;

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of invoking a stream.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Stream(Connector end)
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
        bind(Renderer.class).to(StreamRenderer.class);
        bind(Configuration.class).toProvider(new Provider<Configuration>()
        {
            public Configuration get()
            {
                return new Configuration(methodName, contentType);
            }
        });
    }

    /**
     * Set the method name to call or null if the method is to determined by the
     * mime-type in the {@link Output} annotation.
     * 
     * @param methodName
     *            The method name to call or null if we match on mime type.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Stream methodName(String methodName)
    {
        this.methodName = methodName;
        return this;
    }

    /**
     * Set the mime-type to send.
     * 
     * @param contentType
     *            The mime-type to send.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Stream contentType(String contentType)
    {
        this.contentType = contentType;
        return this;
    }
}
