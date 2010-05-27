package com.goodworkalan.paste.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.ilk.inject.InjectorScoped;
import com.goodworkalan.paste.connector.Connector;
import com.goodworkalan.paste.controller.Renderer;
import com.goodworkalan.paste.controller.qualifiers.Controller;

/**
 * An extension element in the domain-specific language that is used to specify
 * a stream rendering response, a response that is the stream output of a method
 * in a controller.
 * 
 * @author Alan Gutierrez
 */
public class Stream {
    /** The parent builder. */
    private final Connector connector;

    // TODO Document.
    private final List<InjectorBuilder> modules;

    // TODO Document.
    private Configuration configuration = new Configuration();

    /**
     * Create an extension to the domain-specific language used to specify the
     * details of invoking a stream.
     * 
     * @param end
     *            The connector to return when the render statement is complete.
     */
    public Stream(Connector connector, List<InjectorBuilder> modules) {
        this.connector = connector;
        this.modules = modules;
    }

    /**
     * Set the method name to call or null if the method is to determined by the
     * mime-type in the {@link Output} annotation.
     * 
     * @param methodName
     *            The method name to call or null if we match on mime type.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Stream methodName(String methodName) {
        configuration.methodName = methodName;
        return this;
    }

    /**
     * Set the mime-type to send.
     * 
     * @param contentType
     *            The mime-type to send.
     * @return This domain-specific language extension to continue specifying
     *         redirect properties.
     */
    public Stream contentType(String contentType) {
        configuration.contentType = contentType;
        return this;
    }

    public Connector end() {
        modules.add(new InjectorBuilder() {
            protected void build() {
                reflector(new IlkReflect.Reflector() {
                    public Object newInstance(Constructor<?> constructor, Object[] arguments)
                    throws InstantiationException, IllegalAccessException, InvocationTargetException {
                        return constructor.newInstance(arguments);
                    }
                });
                instance(configuration, ilk(Configuration.class), Controller.class);
                implementation(ilk(StreamRenderer.class), ilk(Renderer.class), null, InjectorScoped.class);
            }
        });
        configuration = null;
        return connector;
    }
}
