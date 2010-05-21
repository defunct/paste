package com.goodworkalan.paste.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used by controllers to specify the {@link Actor} classes that will manipulate
 * the controller during a request.
 *
 * @author Alan Gutierrez
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Actors {
    /**
     * This list of actors that will be executed before the controller is created.
     * The controller parameters and controller class will be available, but the controller
     * will not yet be instanciated and should not be instanciated.
     */
    Class<? extends Runnable>[] before() default {};
 
    /**
     * The list of actors that will act upon the controller after the controller
     * has been created.
     * 
     * @return The list of actors.
     */
    Class<? extends Runnable>[] value() default {};

    /**
     * This list of actors that will act upon the controller after the
     * controller has been created and after the controllers of the
     * <code>value</code> value property have been run.
     * <p>
     * The the two sets of after actors are offered for aesthetics. If there
     * are not before actors, then you can just use the value property. If there
     * are before actors you can use the after property to balance the before
     * property.
     */
    Class<? extends Runnable>[] after() default {};
}
