package com.goodworkalan.paste.connector;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.goodworkalan.ilk.inject.InjectorBuilder;
import com.goodworkalan.paste.controller.PasteException;
import com.goodworkalan.paste.servlet.BindKey;
import com.goodworkalan.reflective.Reflective;
import com.goodworkalan.reflective.ReflectiveException;
import com.goodworkalan.winnow.Condition;
import com.goodworkalan.winnow.Equals;
import com.goodworkalan.winnow.InstanceOf;
import com.goodworkalan.winnow.RuleMapBuilder;
import com.goodworkalan.winnow.RuleSetBuilder;

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
    protected final RuleMapBuilder<BindKey, List<InjectorBuilder>> mappings;

    /** A builder for a set of rules for this render statement. */
    protected final RuleSetBuilder<BindKey, List<InjectorBuilder>> from;

    /**
     * Create a render statement.
     * 
     * @param end
     *            The connector to return when the statement terminates.
     * @param mappings
     *            A builder for a map of rule sets render modules.
     */
    RenderStatement(Connector end, RuleMapBuilder<BindKey, List<InjectorBuilder>> mappings) {
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
    
    public RenderStatement path(final String... contentTypes) {
        from.check(BindKey.PATH, new Condition() {
            public boolean test(Object object) {
                for (String contentType : contentTypes) {
                    if (Pattern.matches(contentType, (String) object)) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }
    
    public RenderStatement contentType(String... contentTypes) {
        for (String contentType : contentTypes) {
            from.check(BindKey.CONTENT_TYPE, new Equals(contentType));
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
     * Specify the suffixes that this rule matches.
     * 
     * @param suffixes
     *            The suffixes.
     * @return This when statement to continue specifying rules.
     */
    public RenderStatement suffix(String...suffixes) {
        for (String suffix : suffixes) {
            from.check(BindKey.SUFFIX, new Equals(suffix));
        }
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
        final List<InjectorBuilder> modules = new ArrayList<InjectorBuilder>();
        from.put(modules);
        try {
            try {
                Constructor<T> constructor = renderClass.getConstructor(Connector.class, List.class);
                return constructor.newInstance(end, modules);
            } catch (Throwable e) {
                throw new ReflectiveException(Reflective.encode(e), e);
            }
        } catch (ReflectiveException e) {
            throw new PasteException(0, e);
        }
    }
}
