package com.goodworkalan.paste.controller.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.goodworkalan.paste.controller.Headers;

/**
 * Indicates that an object is associated with the HTTP response. Used to
 * disambiguate the request and response instances of {@link Headers}, for
 * example.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Response {
}