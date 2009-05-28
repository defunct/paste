package com.goodworkalan.paste.stream;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.Renderer;
import com.goodworkalan.paste.Responder;
import com.goodworkalan.paste.Response;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

public class StreamRenderer implements Renderer
{
    /** 
     * The properties set by the domain-specific renderer connection language.
     */
    private final Configuration configuration;
    
    /** The final controller for the request. */
    private final Object controller;
    
    /** The response for the request. */
    private final Response response;
    
    /** The output conduit for the request. */
    private final Responder responder;
    
    /** The Guice injector. */
    private final Injector injector;

    /**
     * Create a stream renderer that will stream the output from a method from
     * the given controller. The renderer will write the given response headers
     * to the given HTTP response before streaming the contorller output to the
     * given HTTP response.
     * 
     * @param configuration
     *            The properties set by the domain-specific renderer connection
     *            language.
     * @param controller
     *            The final controller for the request.
     * @param headers
     *            The response headers for the request.
     * @param response
     *            The HTTP response for the request.
     * @param injector
     *            The Guice injector.
     */
    @Inject
    public StreamRenderer(Configuration configuration, @Controller Object controller, Response response, Responder responder, Injector injector)
    {
        this.configuration = configuration;
        this.controller = controller;
        this.responder = responder;
        this.response = response;
        this.injector = injector;
    }
    
    public void render() throws ServletException, IOException
    {
        Class<? extends Object> controllerClass = controller.getClass();
        
        Method outputMethod = null;
        for (Method method : controllerClass.getMethods())
        {
            Output output = method.getAnnotation(Output.class);
            if (output != null)
            {
                if (output.contentType().equals(configuration.getContentType()))
                {
                    outputMethod = method;
                    break;
                }
            }
        }
        
        Class<?>[] types = outputMethod.getParameterTypes();
        Annotation[][] annotations = outputMethod.getParameterAnnotations();
        Object[] parameters = new Object[types.length];
        for (int i = 0; i < types.length; i++)
        {
            Class<?> type = outputMethod.getParameterTypes()[i];
            Annotation annotation = null;
            for (int j = 0; annotation == null && j < annotations[i].length; j++)
            {
                if (annotations[i][j].getClass().getAnnotation(BindingAnnotation.class) != null)
                {
                    annotation = annotations[i][j];
                }
            }
            if (annotation == null)
            {
                parameters[i] = injector.getInstance(type);
            }
            else
            {
                parameters[i] = injector.getInstance(Key.get(type, annotation));
            }
        }
        
        Object result;
        try
        {
            result = outputMethod.invoke(controller, parameters);
        }
        catch (Exception e)
        {
            throw new PasteException(e);
        }
        
        response.addHeader("Content-Type", configuration.getContentType());

        responder.send(response);
        
        if (result instanceof URI)
        {
            URI uri = (URI) result;
            HttpServletRequest request = injector.getInstance(HttpServletRequest.class);
            URL resource = request.getServletContext().getResource(uri.getPath());
            InputStream in = resource.openStream();
            byte[] buffer = new byte[4096];
            int read;
            while ((read = in.read(buffer)) != -1)
            {
                responder.getOutputStream().write(buffer, 0, read);
            }
        }
        else if (result instanceof String)
        {
            responder.getWriter().write((String) result);
        }
    }
}
