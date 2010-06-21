package com.goodworkalan.paste.redirect;

import static com.goodworkalan.paste.redirect.Redirects.isRedirectStatus;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;

/**
 * An extension element in a domain-specific language use to specify the details
 * of an HTTP redirection.
 * 
 * @author Alan Gutierrez
 */
public class Redirect {
    /** The parent builder. */
    private final Connector connector;

    /** The modules used to define the renderer. */
    private final List<InjectorBuilder> modules;

    /** The structure containing the configuration. */
    private final Configuration configuration = new Configuration();

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of an HTTP redirection.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Redirect(Connector connector, List<InjectorBuilder> modules) {
        this.connector = connector;
        this.modules = modules;
    }

    /**
     * Set the status code to set during the redirection.
     * 
     * @param status
     *            The status code to set during the redirection.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Redirect status(int status) {
        if (!isRedirectStatus(status)) {
            throw new IllegalArgumentException();
        }
        configuration.status = status;
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
    public Redirect format(String format, Class<?>... formatArguments) {
        configuration.format = format;
        configuration.formatArguments = formatArguments;
        return this;
    }

    /**
     * Terminate the redirect specification and return the parent
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
                implementation(ilk(RedirectRenderer.class), ilk(Renderer.class), null, InjectorScoped.class);
            }
        });
        return connector;
    }
}
