package com.goodworkalan.paste;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.deviate.Equals;
import com.goodworkalan.deviate.InstanceOf;
import com.goodworkalan.deviate.RuleMapBuilder;
import com.goodworkalan.deviate.RuleSetBuilder;
import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.reflective.Reflection;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;
import com.mallardsoft.tuple.Pair;

/**
 * An element in the controller connection domain-specific language that
 * specifies which rendering module is used to render controllers, errors and
 * execptions.
 * 
 * @author Alan Gutierrez
 */
public class RenderStatement {
    /** The connector to return when the statement terminates. */
    private final Connector end;
    
    /** A builder for a map of rule sets to priority and render modules pairs. */
    protected final RuleMapBuilder<Pair<Integer, List<InjectorBuilder>>> mappings;

    /** A builder for a set of rules for this render statement. */
    protected final RuleSetBuilder<Pair<Integer, List<InjectorBuilder>>> from;

    /**
     * The priority for this render statement to resolve ambiguties if two or
     * more rule sets match a response.
     */
    private int priority;

    /**
     * Create a render statement.
     * 
     * @param end
     *            The connector to return when the statement terminates.
     * @param mappings
     *            A builder for a map of rule sets to priority and render
     *            modules pairs.
     */
    public RenderStatement(Connector end, RuleMapBuilder<Pair<Integer, List<InjectorBuilder>>> mappings) {
        this.end = end;
        this.mappings = mappings;
        this.from = mappings.rule();
    }

    /**
     * Match the controllers that are an instance of the given class.
     * 
     * @param controllerClass
     *            The controller class.
     * @return This render statement to continue to specify match criteria.
     */
    public RenderStatement controller(Class<?> controllerClass) {
        from.check(BindKey.CONTROLLER_CLASS, new InstanceOf(controllerClass));
        return this;
    }

    /**
     * Match the given HTTP response status code.
     * 
     * @param status
     *            The status code.
     * @return This render statement to continue to specify match criteria.
     */
    public RenderStatement status(int status) {
        from.check(BindKey.STATUS, new Equals(status));
        return this;
    }

    /**
     * Match the given HTTP request methods.
     * 
     * @param methods
     *            The HTTP request methods.
     * @return This render statement to continue to specify match criteria.
     */
    public RenderStatement method(String... methods) {
        for (String method : methods) {
            from.check(BindKey.METHOD, new Equals(method));
        }
        return this;
    }

    /**
     * Match the excptions that are an instance of the given class.
     * 
     * @param exceptionClass
     *            The exception class.
     * @return This render statement to continue to specify match criteria.
     */
    public RenderStatement exception(Class<? extends Throwable> exceptionClass) {
        from.check(BindKey.EXCEPTION_CLASS, new InstanceOf(exceptionClass));
        return this;
    }

    /**
     * Assign a priority for this render statement to resolve ambiguties if two
     * or more rule sets match a response.
     * 
     * @param priority
     *            The priority.
     * @return This render statement to continue to specify match criteria.
     */
    public RenderStatement priority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * Specify the render module that will render a controller or exception when
     * the criteria specified by this render statement is matched.
     * 
     * @param renderClass
     *            The render module class.
     * @return The render module as a language element to specify rendering
     *         properties.
     * @param <T>
     *            The type of render module.
     */
    public <T> T with(final Class<T> renderClass) {
        Reflective reflective = new Reflective();
        final List<InjectorBuilder> modules = new ArrayList<InjectorBuilder>();
        from.put(new Pair<Integer, List<InjectorBuilder>>(priority, modules));
        try {
            return reflective.reflect(new Reflection<T>() {
                public T reflect() throws InstantiationException,
                         IllegalAccessException, InvocationTargetException,
                         NoSuchFieldException, NoSuchMethodException {
                    Constructor<T> constructor = renderClass.getConstructor(Connector.class, List.class);
                    return constructor.newInstance(end, modules);
                 } 
             });
        } catch (ReflectiveException e) {
            throw new PasteException(0, e);
        }
    }
}
