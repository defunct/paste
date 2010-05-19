package com.goodworkalan.paste.redirect;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.servlet.ServletException;

import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.Response;
import com.goodworkalan.paste.paths.PathFormatter;

/**
 * Render a redirect by sending a redirect status code and headers to the client.
 *
 * @author Alan Gutierrez
 */
public class RedirectRenderer implements Renderer {
    /** The path formatter used to create the redirection path. */
    private final PathFormatter pathFormatter;

    /** The redirector service. */
    private final Redirector redirector;

    /** The alternative HTTP response. */
    private final Response response;

    /** The redirect renderer configuration. */
    private final Configuration configuration;

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
    public RedirectRenderer(PathFormatter pathFormatter, Response response, Redirector redirector, Configuration configuration) {
        this.pathFormatter = pathFormatter;
        this.response = response;
        this.redirector = redirector;
        this.configuration = configuration;
    }

    /**
     * Write a redirection header and body to the HTTP response.
     */
    public void render() throws ServletException, IOException {
        if (configuration.format != null) {
            String path = pathFormatter.format(configuration.format, configuration.formatArguments);
            redirector.redirect(path, configuration.status);
        }

        if (!Redirects.isRedirectStatus(response.getStatus())) {
            throw new PasteException(0);
        } else if (response.getHeaders().getFirst("Location") == null) {
            throw new PasteException(0);
        } else {
            try {
                new URI(response.getHeaders().getFirst("Location"));
            } catch (URISyntaxException e) {
                throw new PasteException(0, e);
            }
        }

        String page = String.format(getPageFormat(), response.getStatus(), response.getHeaders().getFirst("Location"));
        response.getWriter().append(page);
    }

    /**
     * Read the page format for the redirection page from a resource file.
     * 
     * @return The page format for the redirection page.
     * @throws IOException
     *             If an I/O error occurs.
     */
    private String getPageFormat() throws IOException {
        Reader reader = new InputStreamReader(RedirectRenderer.class
                .getResourceAsStream("redirect.html"));
        StringBuilder newString = new StringBuilder();
        char[] buffer = new char[2048];
        int read;
        while ((read = reader.read(buffer)) != -1) {
            newString.append(buffer, 0, read);
        }
        return newString.toString();
    }
}
