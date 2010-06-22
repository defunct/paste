package com.goodworkalan.paste.stencil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;

/**
 * A renderer extension that specifies rendering using Stencil. 
 *
 * @author Alan Gutierrez
 */
public class Stencil {
    /** The parent builder. */
    private final Connector connector;
    
    /** The modules used to define the renderer. */
	private final List<InjectorBuilder> modules;

	/** The configuration. */
    private Configuration configuration = new Configuration();

    /**
	 * Create a new Stencil with the given connector parent builder.
	 * 
	 * @param connector
	 *            The parent builder.
	 */
    public Stencil(Connector connector, List<InjectorBuilder> modules) {
    	this.connector = connector;
    	this.modules = modules;
    }

    /**
     * Set the mime-type to send.
     * 
     * @param contentType
     *            The mime-type to send.
     * @return This domain-specific language extension to continue specifying
     *         Stencil properties.
     */
    public Stencil contentType(String contentType) {
        configuration.contentType = contentType;
        return this;
    }

    /**
     * Set the format string and the format arguments used to create the
     * Stencil URI. Format arguments are objects that generate argument
     * values using the Guice injector, to replace format parameters with values
     * from the servlet environment.
     * 
     * @param format
     *            The format string.
     * @param formatArguments
     *            The format arguments.
     * @return This domain-specific language extension to continue specifying
     *         Stencil properties.
     */
    public Stencil format(String format, Class<?>... formatArguments) {
        configuration.format = format;
        configuration.formatArguments = formatArguments;
        return this;
    }

	/**
	 * Set a base URI for resolution of Stencil files, or null to indicate the
	 * URI of the web application.
	 * 
	 * @param baseURI
	 *            The base URI.
	 * @return This domain-specific language extension to continue specifying
	 *         Stencil properties.
	 */
    public Stencil baseURI(URI baseURI) {
    	configuration.baseURI = baseURI;
    	return this;
    }

    /**
     * Terminate the Stencil specification and return the parent
     * <code>Connector</code> to continue specifying routes and renderers.
     * 
     * @return The parent <code>Connector</code> to continue specifying routes
     *         and renderers.
     */
    public Connector end() {
        modules.add(new InjectorBuilder() {
            protected void build() {
                reflector(new IlkReflect.Reflector() {
                    public Object newInstance(Constructor<?> constructor, Object[] arguments)
                    throws InstantiationException, IllegalAccessException, InvocationTargetException {
                        return constructor.newInstance(arguments);
                    }
                });
                instance(configuration, ilk(Configuration.class), null);
                implementation(ilk(StencilRenderer.class), ilk(Renderer.class), null, InjectorScoped.class);
            }
        });
        return connector;
    }
}
