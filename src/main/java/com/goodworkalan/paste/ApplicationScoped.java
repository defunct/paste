package com.goodworkalan.paste;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

/**
 * Indicates that an object should be created in the application scope, so that
 * it exists for the lifetime of the application.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface ApplicationScoped {
}
