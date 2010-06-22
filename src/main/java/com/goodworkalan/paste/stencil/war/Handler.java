package com.goodworkalan.paste.stencil.war;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.servlet.ServletContext;

/**
 * A URL stream handler for a WAR protocol that will return resources from the
 * WAR of a web application from within the web application. This
 * <code>URLStreamHandler</code> is used by {@link WarResourceResolver} to build
 * a <code>URL</code> that can read from a web application's archive using the
 * web application's {@link ServletContext}.
 * <p>
 * This handler can be used to create a <code>URL</code> using the {@link URL
 * URL(URL, String, URLStreamHandler)} constructor. The URL constructor will
 * bypass the global protocol resolution, so that the unknown protocol scheme
 * will not raise an exception. The <code>URL</code> can then be used like any
 * other URL.
 * 
 * @author Alan Gutierrez
 */
public class Handler extends URLStreamHandler {
	/** The servlet context. */
	private final ServletContext servletContext;

	/**
	 * Create a WAR protocol handler that will return resources from the WAR of
	 * the given servlet context.
	 * 
	 * @param servletContext
	 *            The servlet context.
	 */
	public Handler(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

    /**
     * Open a connection using the given URL. The path of the URL will be used
     * to obtain a resource from the <code>ServletContext</code> property.
     * 
     * @param u
     *            The URL to open.
     * @return An open <code>URLConnection</code>.
     * @throws IOException
     *             If the connection fails or if the resource indicated by the
     *             URL cannot be found.
     */
    @Override
    public URLConnection openConnection(URL u) throws IOException {
    	URL url = servletContext.getResource(u.getPath());
    	if (url == null) {
    		throw new IOException();
    	}
    	return url.openConnection();
    }
}