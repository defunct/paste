package com.goodworkalan.stringbeans.json.paste;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import com.goodworkalan.danger.Danger;
import com.goodworkalan.ilk.Ilk;
import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.Boxed;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.stream.Output;
import com.goodworkalan.stringbeans.Converter;
import com.goodworkalan.stringbeans.json.JsonEmitter;

/**
 * Render a controller using String Beans JSON serialization.
 *
 * @author Alan Gutierrez
 */
class JsonRenderer implements Renderer {
    /** The final controller for the request. */
    private final Ilk.Box controller;
    
    /** The dependency injector. */
    private final Injector injector;
    
    /** String Beans emitter and parser configuration. */
    private final Converter converter;
    
    /** The HTTP response. */
    private final HttpServletResponse response;

    /**
     * Create a String Beans JSON renderer.
     * 
     * @param injector
     *            The injector.
     * @param controller
     *            The boxed controller instance.
     * @param converter
     *            The String Beans emitter and parser configuration.
     * @param response
     *            The HTTP response.
     */
    @Inject
    public JsonRenderer(Injector injector, @Controller Boxed<Object> controller, Converter converter, HttpServletResponse response) {
        this.injector = injector;
        this.controller = controller.box;
        this.converter = converter;
        this.response = response;
    }

    /**
     * Serialize to the HTTP response output stream the return value of the
     * first method or the value of the first field encountered that is
     * annotated with {@link Output}.
     * 
     * @throws ServletException
     *             For any Servlet engine error.
     * @throws IOException
     *             For any I/O error.
     */
    public void render() throws ServletException, IOException {
        response.setContentType("application/json");
        Ilk.Box output = null;
        for (Method method : controller.object.getClass().getMethods()) {
            if (method.getAnnotation(Output.class) != null) {
                try {
                    output = injector.inject(IlkReflect.REFLECTOR, controller, method);
                } catch (InvocationTargetException e) {
                    throw new ControllerException(e);
                } catch (IllegalAccessException e) {
                    throw new Danger(e, JsonRenderer.class, "getterInaccessible", method.getName());
                }
                break;
            }
        }
        if (output == null) {
            for (Field field : controller.object.getClass().getFields()) {
                if (field.getAnnotation(Output.class) != null) {
                    try {
                        output = IlkReflect.get(IlkReflect.REFLECTOR, field, controller);
                    } catch (IllegalAccessException e) {
                        throw new Danger(e, JsonRenderer.class, "fieldInaccessible", field.getName());
                    }
                    break;
                }
            }
        }
        JsonEmitter emitter = new JsonEmitter(converter);
        emitter.emit(response.getWriter(), output.object);
    }
}
