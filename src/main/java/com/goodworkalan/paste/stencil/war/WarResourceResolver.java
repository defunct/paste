package com.goodworkalan.paste.stencil.war;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.servlet.ServletContext;

import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.stencil.ResourceResolver;

/**
 * Obtains resources from the web application archive of a web application from
 * within the web application.
 * <p>
 * The <code>Injector</code> given to the <code>StencilFactory</code> must be
 * able to provide a <code>ServletContext</code> that can be used to obtain
 * resources from a web application archive of a running web application.
 */
public class WarResourceResolver implements ResourceResolver {
	/**
	 * Get the war URL for the given uri.
	 * 
	 * @param injector
	 *            The injector.
	 * @param uri
	 *            The URI.
	 */
	public URL getURL(Injector injector, URI uri) throws MalformedURLException {
		ServletContext servletContext = injector.instance(ServletContext.class, null);
		return new URL(null, uri.toString(), new Handler(servletContext));
	}
}
