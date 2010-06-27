package com.goodworkalan.paste.redirect;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.controller.Redirection;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.paths.PathFormatter;

/**
 * Render a redirect by sending a redirect status code and headers to the client.
 *
 * @author Alan Gutierrez
 */
class RedirectRenderer implements Renderer {
    /** The path formatter used to create the redirection path. */
    private final PathFormatter pathFormatter;
    
    /** The HTTP response. */
    private final HttpServletResponse response;

    /** The thrown redirection exception. */ 
    private final Throwable throwable; 
    
    /** The redirect renderer configuration. */
    private final Configuration configuration;
    
    /** The HTTP request. */
    private final HttpServletRequest request;

    /**
     * Create a redirect renderer with the given path formatter, the given
     * response service, the given redirection service and the given redirect
     * renderer configuration.
     * 
     * @param configuration
     *            The redirect renderer configuration.
     * @param pathFormatter
     *            The path formatter used to create the redirection path.
     * @param request
     *            The HTTP request.
     * @param response
     *            The HTTP response.
     * @param throwable
     *            The thrown redirection exception.
     */
    @Inject
    public RedirectRenderer(Configuration configuration, PathFormatter pathFormatter, HttpServletRequest request, HttpServletResponse response, @Controller Throwable throwable) {
        this.pathFormatter = pathFormatter;
        this.request = request;
        this.response = response;
        this.throwable = throwable;
        this.configuration = configuration;
    }
    
    /** Generate the dredirect. */
    public void render() throws ServletException, IOException {
        Redirection redirection = (Redirection) throwable;
        int status = redirection.status;

        if (!Redirect.isRedirectStatus(status)) {
            throw new IllegalArgumentException();
        }

        response.setStatus(status);

        String format = redirection.whereFormat;
        if (format == null) {
            format = configuration.format;
        }
        Class<?>[] formatArguments = redirection.whereFormatArguments;
        if (formatArguments == null) {
            formatArguments = configuration.formatArguments;
        }
        String where = pathFormatter.format(format, formatArguments);
        URI location = URI.create(where.trim());
        String resolved = null; 
        if (location.getScheme() == null) {
            URI uri = URI.create(request.getRequestURL().toString());
            String path = location.getPath();
            if (path.length() != 0 && path.charAt(0) == '/') {
                path = request.getContextPath() + path;
                resolved =  uri.resolve(path).toString();
            } else {
                resolved =  uri.resolve(path).toString();
            }
        } else {
            resolved =  location.toString();
        }
        response.addHeader("Location", resolved);
        try {
            String page = String.format(getPageFormat(RedirectRenderer.class.getResourceAsStream("redirect.html")), redirection.status, resolved);
            response.getWriter().append(page);
        } catch (IOException e) {
            throw new RuntimeException("Unable to render redirection error page.", e);
        }
    }

    /**
     * Read the page format for the redirection page from a resource file.
     * 
     * @param in
     *            The resource file input stream.
     * @return The page format for the redirection page.
     * @throws IOException
     *             If an I/O error occurs.
     */
    String getPageFormat(InputStream in) throws IOException {
        Reader reader = new InputStreamReader(in);
        StringBuilder newString = new StringBuilder();
        char[] buffer = new char[2048];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            newString.append(buffer, 0, read);
        }
        return newString.toString();
    }
}
