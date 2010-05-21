package com.goodworkalan.paste.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates that an object will last the lifetime of the application. Used by
 * application programmers to implement singletons. Used by the list of janitors
 * that will clean up when the application terminates.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Application {
}
