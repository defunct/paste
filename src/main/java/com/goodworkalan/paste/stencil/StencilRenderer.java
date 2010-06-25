package com.goodworkalan.paste.stencil;

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.paths.PathFormatter;
import com.goodworkalan.paste.stencil.war.WarResourceResolver;
import com.goodworkalan.stencil.StencilFactory;

/**
 * Render a controller using Stencil.
 * 
 * @author Alan Gutierrez
 */
public class StencilRenderer implements Renderer {
    /** The cache of Stencil factories. */
	private final static Map<Configuration, StencilFactory> FACTORIES = new ConcurrentHashMap<Configuration, StencilFactory>();

	/** The path formatter used to create the redirection path. */
    private final PathFormatter pathFormatter;
    
    /** The HTTP response. */
    private final HttpServletResponse response;

    /** The redirect renderer configuration. */
    private final Configuration configuration;
    
    /** The injector. */
    private final Injector injector;

	/**
	 * Create a redirect renderer with the given path formatter, the given
	 * response service, the given redirection service and the given redirect
	 * renderer configuration.
	 * 
	 * @param injector
	 *            The injector.
	 * @param pathFormatter
	 *            The path formatter used to create the redirection path.
	 * @param response
	 *            The alternative HTTP response service.
	 * @param redirector
	 *            The redirector service.
	 * @param configuration
	 *            The redirect renderer configuration.
	 */
    @Inject
    public StencilRenderer(Injector injector, PathFormatter pathFormatter, HttpServletResponse response, Configuration configuration) {
    	this.injector = injector;
        this.pathFormatter = pathFormatter;
        this.response = response;
        this.configuration = configuration;
    }

    /**
     * Create a new transformer handler using the given transformer factory.
     * This method has been extracted in order to test the handing of the exceptions
     * raised by the transformer factory.
     *  
     * @param stf A SAX transformer factory.
     * @return A new transformer handler.
     */
    static TransformerHandler newTransformerHandler(SAXTransformerFactory stf) {
        try {
            return stf.newTransformerHandler();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
	}

    /** Render the controller. */
    public void render() throws ServletException, IOException {
        if (configuration.contentType != null) {
            response.setContentType(configuration.contentType);
        }
        URI baseURI = configuration.baseURI;
        if (baseURI == null) {
            baseURI = URI.create("war:///");
        }
        baseURI = baseURI.normalize();
        StencilFactory stencils = FACTORIES.get(configuration);
        if (stencils == null) {
            stencils = new StencilFactory();
            stencils.setBaseURI(baseURI);
            stencils.addResolver("war", new WarResourceResolver());
            FACTORIES.put(configuration, stencils);
        }
        URI uri = URI.create(pathFormatter.format(configuration.format, configuration.formatArguments));
        stencils.stencil(injector, uri, response.getWriter());
	}
}
