package com.goodworkalan.stringbeans.json.paste;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;

/**
 * A renderer extension that specifies rendering using String Beans JSON
 * serialization.
 * 
 * @author Alan Gutierrez
 */
public class Json { 
    /** The parent builder. */
    private final Connector connector;
    
    /** The modules used to define the renderer. */
    private final List<InjectorBuilder> modules;

    /**
     * Create an instance of the builder language extension to specify JSON
     * conversion of a controller output.
     * 
     * @param connector
     *            The parent builder.
     * @param modules
     *            The modules used to define the renderer.
     */
    public Json(Connector connector, List<InjectorBuilder> modules) {
        this.connector = connector;
        this.modules = modules;
    }

    /**
     * Terminate the JSON specification and return the parent
     * <code>Connector</code> to continue specifying routes and renderers.
     * 
     * @return The parent <code>Connector</code> to continue specifying routes
     *         and renderers.
     */
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