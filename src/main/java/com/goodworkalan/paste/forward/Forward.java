package com.goodworkalan.paste.forward;

import static com.goodworkalan.paste.paths.FormatArguments.CONTROLLER_CLASS_AS_PATH;

import com.goodworkalan.paste.Connector;
import com.goodworkalan.paste.ControllerScoped;
import com.goodworkalan.paste.RenderModule;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.paths.FormatArgument;
import com.google.inject.Provider;

/**
 * An extension element in a domain-specific language used to specify the
 * details of rendering the controller or exception by forwarding it to another
 * filter or servlet.
 * 
 * @author Alan Gutierrez
 */
public class Forward extends RenderModule {
    /** The format to use to create the forward path. */
    private String format = "/%s.ftl";

    /** The request property name to use to store the controller. */
    private String property = "controller";

    /** The format arguments to use to create the forward path. */
    private FormatArgument[] formatArguments = new FormatArgument[] { CONTROLLER_CLASS_AS_PATH };

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of forwarding a controller to another filter or servlet.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Forward(Connector end) {
        super(end);
    }

    /**
     * Configure the a Guice child injector to include the properties necessary
     * to create a {@link Renderer} that will forward the request to another
     * filter or servlet.
     */
    @Override
    protected void configure() {
        bind(Renderer.class).to(ForwardRenderer.class).in(ControllerScoped.class);
        bind(Configuration.class).toProvider(new Provider<Configuration>() {
            public Configuration get() {
                return new Configuration(property, format, formatArguments);
            }
        }).in(ControllerScoped.class);
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
        this.property = property;
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
    public Forward format(String format, FormatArgument... formatArguments) {
        this.format = format;
        this.formatArguments = formatArguments;
        return this;
    }
}
