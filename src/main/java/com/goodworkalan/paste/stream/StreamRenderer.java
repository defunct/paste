package com.goodworkalan.paste.stream;

import static com.goodworkalan.ilk.Types.getRawClass;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.Boxed;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.scopes.ControllerScoped;

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
class StreamRenderer implements Renderer {
    /**
     * The properties set by the domain-specific renderer connection language.
     */
    private final Configuration configuration;
    
    /** The final controller for the request. */
    private final Boxed<Object> controller;
    
    /** The dependency injector. */
    private final Injector injector;

    /**
     * Create a stream renderer that will stream the output from a method from
     * the given controller. The renderer will write the given response headers
     * to the given HTTP response before streaming the controller output to the
     * given HTTP response.
     * 
     * @param configuration
     *            The properties set by the domain-specific renderer connection
     *            language.
     * @param controller
     *            The final controller for the request.
     * @param injector
     *            The injector.
     */
    @Inject
    public StreamRenderer(Configuration configuration, @Controller Boxed<Object> controller, Injector injector) {
        this.configuration = configuration;
        this.controller = controller;
        this.injector = injector;
    }

    /**
     * Render output by calling a method on the controller that either returns a
     * character sequence or accepts an output stream or writer to stream the
     * output.
     */
    public void render() throws ServletException, IOException {
        Class<? extends Object> controllerClass = controller.getClass();

        Method outputMethod = null;
        for (Method method : controllerClass.getMethods()) {
            Output output = method.getAnnotation(Output.class);
            if (output != null) {
                if (output.contentType().equals(configuration.contentType)) {
                    outputMethod = method;
                    break;
                }
            }
        }
        
        Ilk.Box box;
        try {
            box = injector.inject(IlkReflect.REFLECTOR, controller.box, outputMethod);
        } catch (InvocationTargetException e) {
            throw new ControllerException(e);
        } catch (IllegalAccessException e) {
            String message = String.format(
                "\n\tMember annotated for stream output is inaccessible." +
                "\n\t\tController [%s], Property [%s]", controllerClass.getName(), outputMethod.getName());
            throw new RuntimeException(message, e);
        } 

        HttpServletResponse response = injector.instance(HttpServletResponse.class, null);
        response.setContentType(configuration.contentType);
        
        if (URI.class.isAssignableFrom(getRawClass(box.key.type))) {
            URI uri = box.cast(new Ilk<URI>(URI.class));
            HttpServletRequest request = injector.instance(HttpServletRequest.class, null);
            RequestDispatcher dispatcher = request.getRequestDispatcher(uri.getPath());
            dispatcher.forward(request, response);
        } else  if (CharSequence.class.isAssignableFrom(getRawClass(box.key.type))) {
            response.getWriter().write(box.cast(new Ilk<CharSequence>(CharSequence.class)).toString());
            response.getWriter().flush();
        }
    }
}
