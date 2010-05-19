package com.goodworkalan.paste;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates that an object was created based on the matched controller. Used
 * to annotate the controller object itself and the map of parameters matched
 * by the controller's URL binding pattern.
 *
 * @author Alan Gutierrez
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Qualifier
public @interface Controller {
}
