package com.goodworkalan.paste.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that a method should be invoked after a controller is constructed.
 * If no request methods or trigger values are specified, the method is always
 * invoked after the controller is constructed.
 * 
 * @author Alan Gutierrez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invoke {
    /**
     * The parameter values which cause the method to be invoked, or the
     * parameter name whose cause the method to be invoked if the
     * <code>param</code> property is not specified.
     * 
     * @return The values on which the method is inovked.
     */
    String[] on() default {};

    /**
     * The parameter to use for the trigger value, or blank to interpret the
     * trigger value as parameter names, as opposed to a parameter value.
     * 
     * @return The parameter name.
     */
    String param() default "";

    /**
     * The HTTP method verbs that will cause the annotated method to be invoked.
     * 
     * @return The HTTP methods.
     */
    String[] methods() default {};
}
