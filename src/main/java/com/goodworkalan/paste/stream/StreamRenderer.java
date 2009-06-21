package com.goodworkalan.paste.stream;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URI;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.ControllerScoped;
import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.Renderer;
import com.google.inject.BindingAnnotation;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Render output by calling a method on the controller that either returns a
 * character sequence or accepts an output stream or writer to stream the
 * output. The renderer will look for methods in the controller annotated with
 * {@link Output}. The renderer will match the content type specified in the
 * domain-specific language to the content type specified in the annotation.
 * <p>
 * If an annotated method returns a <code>CharSequence</code>, the return value
 * is used as the stream response. If an annotated method accepts an
 * <code>OutputStream</code> the <code>OutputStream</code> of the
 * <code>HttpServletResponse</code> is passed to the method. If an annotated
 * method accepts a <code>Writer</code> the <code>PrintWriter</code> of the
 * <code>HttpServletResponse</code> is passed to the method.
 * 
 * @author Alan Gutierrez
 */
@ControllerScoped
public class StreamRenderer implements Renderer
{
    /** 
     * The properties set by the domain-specific renderer connection language.
     */
    private final Configuration configuration;
    
    /** The final controller for the request. */
    private final Object controller;
    
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
    public StreamRenderer(Configuration configuration, @Controller Object controller, Injector injector)
    {
        this.configuration = configuration;
        this.controller = controller;
        this.injector = injector;
    }

    /**
     * Render output by calling a method on the controller that either returns a
     * character sequence or accepts an output stream or writer to stream the
     * output.
     */
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
            // FIXME No.
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
        
        HttpServletResponse response = injector.getInstance(HttpServletResponse.class);
        response.setContentType(configuration.getContentType());
        
        if (result instanceof URI)
        {
            URI uri = (URI) result;
            HttpServletRequest request = injector.getInstance(HttpServletRequest.class);
            RequestDispatcher dispatcher = request.getRequestDispatcher(uri.getPath());
            dispatcher.forward(request, response);
        }
        else if (result instanceof CharSequence)
        {
            response.getWriter().write(((CharSequence) result).toString());
            response.getWriter().flush();
        }
    }
}
