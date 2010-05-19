package com.goodworkalan.paste;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * Indicates that an object should be created in the controller scope, so that
 * it exists for the lifetime of the controller created for the currently active
 * filtration.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface ControllerScoped {
}
