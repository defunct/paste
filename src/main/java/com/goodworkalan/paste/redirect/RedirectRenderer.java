package com.goodworkalan.paste.redirect;

import static com.goodworkalan.paste.redirect.Redirects.isRedirectStatus;

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
    // TODO Document.
    private final PathFormatter pathFormatter;
    // TODO Document.
    private final HttpServletResponse response;
    // TODO Document.
    private final Throwable throwable; 
    // TODO Document.
    private final Configuration configuration;
    // TODO Document.
    private final HttpServletRequest request;
    /**
     * Create a redirect renderer with the given path formatter, the given
     * response service, the given redirection service and the given redirect
     * renderer configuration.
     * 
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
    public RedirectRenderer(PathFormatter pathFormatter, HttpServletResponse response, @Controller Throwable throwable, Configuration configuration, HttpServletRequest request) {
        this.pathFormatter = pathFormatter;
        this.request = request;
        this.response = response;
        this.throwable = throwable;
        this.configuration = configuration;
    }
    
    // TODO Document.
    public void render() throws ServletException, IOException {
        Redirection redirection = (Redirection) throwable;
        int status = redirection.status;

        if (!isRedirectStatus(status)) {
            throw new IllegalArgumentException();
        }

        response.setStatus(status);

        String format = redirection.format;
        if (format == null) {
            format = configuration.format;
        }
        Class<?>[] formatArguments = redirection.formatArguments;
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
