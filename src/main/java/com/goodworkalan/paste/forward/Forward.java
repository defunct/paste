package com.goodworkalan.paste.forward;

import java.util.List;

import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;

/**
 * An extension element in a domain-specific language used to specify the
 * details of rendering the controller or exception by forwarding it to another
 * filter or servlet.
 * 
 * @author Alan Gutierrez
 */
public class Forward  {
    /** The parent builder. */
    private final Connector connector;

    /** The modules used to define the renderer. */
    private final List<InjectorBuilder> modules;
    
    /** The configuration. */
    private final Configuration configuration = new Configuration();

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of forwarding a controller to another filter or servlet.
     * 
     * @param connector
     *            The parent builder.
     * @param modules
     *            The modules used to define the renderer.
     */
    public Forward(Connector connector, List<InjectorBuilder> modules) {
        this.connector = connector;
        this.modules = modules;
    }

    /**
     * Set the name of the request property to use to store the controller.
     * 
     * @param property
     *            The name of the request property to use to store the
     *            controller.
     * @return This domain-specific language extension to continue specifying
     *         forward properties.
     */
    public Forward property(String property) {
        configuration.property = property;
        return this;
    }

    /**
     * Set the format string and the format arguments used to create the forward
     * path. Format arguments are objects that generate argument values using
     * the Guice injector, to replace format parameters with values from the
     * servlet environment.
     * 
     * @param format
     *            The format string.
     * @param formatArguments
     *            The format arguments.
     * @return This domain-specific language extension to continue specifying
     *         forward properties.
     */
    public Forward format(String format, Class<?>... formatArguments) {
        configuration.format = format;
        configuration.formatArguments = formatArguments;
        return this;
    }

    /**
     * Terminate the forward specification and return the parent
     * <code>Connector</code> to continue specifying routes and renderers.
     * 
     * @return The parent <code>Connector</code> to continue specifying routes
     *         and renderers.
     */
    public Connector end() {
        modules.add(new InjectorBuilder() {
            protected void build() {
                instance(configuration, ilk(Configuration.class), null);
                implementation(ilk(ForwardRenderer.class), ilk(Renderer.class), null, InjectorScoped.class);
            } 
        });
        // If the element is reused you'll get an NPE. (This is enforcement
        // enough. It doesn't build a lot extra into the code to make the
        // configuration
        // separate.
        return connector;
    }
}
