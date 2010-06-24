package com.goodworkalan.stringbeans.json.paste;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;

// TODO Document.
public class Json { 
    // TODO Document.
    private final Connector connector;
    
    // TODO Document.
    private final List<InjectorBuilder> modules;

    /**
     * Create an instance of the builder language extension to specify
     * JSON conversion of a controller output.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Json(Connector connector, List<InjectorBuilder> modules) {
        this.connector = connector;
        this.modules = modules;
    }

    // TODO Document.
    public Connector end() {
        modules.add(new InjectorBuilder() {
            protected void build() {
                reflector(new IlkReflect.Reflector() {
                    public Object newInstance(Constructor<?> constructor, Object[] arguments)
                    throws InstantiationException, IllegalAccessException, InvocationTargetException {
                        return constructor.newInstance(arguments);
                    }
                });
                implementation(ilk(JsonRenderer.class), ilk(Renderer.class), null, InjectorScoped.class);
            }
        });
        return connector;
    }
}
