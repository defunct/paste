package com.goodworkalan.paste.controller.scopes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

/**
 * Indicates that an object should be created in the session scope, so that it
 * exists for the lifetime of the Java Servlet session.
 * 
 * @author Alan Gutierrez
 */
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface SessionScoped {
}
