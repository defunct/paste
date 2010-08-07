package com.goodworkalan.paste.json;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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
import com.goodworkalan.reflective.getter.Getter;
import com.goodworkalan.reflective.getter.Getters;
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
    
    /** The JSON configuration. */
    private final Configuration configuration;

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
     * @param configuration
     *            The JSON configuration.
     */
    @Inject
    public JsonRenderer(Injector injector, @Controller Boxed<Object> controller, Converter converter, HttpServletResponse response, Configuration configuration) {
        this.injector = injector;
        this.controller = controller.box;
        this.converter = converter;
        this.response = response;
        this.configuration = configuration;
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
                    throw new ControllerException(e, controller.getClass());
                } catch (IllegalAccessException e) {
                    throw new Danger(e, JsonRenderer.class, "getterInaccessible", method.getName());
                }
                break;
            }
        }
        String callback = null;
        if (configuration.callback != null) {
            Map<String, Getter> getters = Getters.getGetters(controller.object.getClass());
            Getter getter = getters.get(configuration.callback);
            if (getter == null) {
                throw new Danger(Json.class, "callbackNotFound");
            }
            try {
                callback = (String) getter.get(controller.object);
            } catch (Exception e) {
                throw new Danger(e, Json.class, "cannotGetCallback");
            }
            if (callback != null) {
                response.getWriter().append(callback).append("(");
            }
        }
        if (output == null) {
            for (Field field : controller.object.getClass().getFields()) {
                if (field.getAnnotation(Output.class) != null) {
                    try {
                        output = IlkReflect.get(IlkReflect.REFLECTOR, field, controller);
                    } catch (IllegalAccessException e) {
                        throw new Danger(e, Json.class, "fieldInaccessible", field.getName());
                    }
                    break;
                }
            }
        }
        JsonEmitter emitter = new JsonEmitter(converter);
        emitter.emit(response.getWriter(), output.object);
        if (callback != null) {
            response.getWriter().append(");");
        }
    }
}
