package com.goodworkalan.paste.stream;

import com.goodworkalan.paste.RenderModule;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.ViewBinder;
import com.google.inject.Binder;
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
     * Create a new stream rendering element which will return the given view
     * binder when end is called to continue construction.
     * 
     * @param viewBinder
     *            The view binder element to return when end is called to
     *            continue construction.
     */
    public Stream(ViewBinder viewBinder)
    {
        super(viewBinder);
    }

    /**
     * Configures a {@link Binder} via the exposed methods.
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
     * @return This stream builder element to continue construction.
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
     * @return This stream builder element to continue construction.
     */
    public Stream contentType(String contentType)
    {
        this.contentType = contentType;
        return this;
    }
}
