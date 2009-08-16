package com.goodworkalan.paste;

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
public @interface Actors
{
    /**
     * The list of actors that will act upon the controller.
     * 
     * @return The list of actors.
     */
    Class<? extends Actor>[] value();
}
