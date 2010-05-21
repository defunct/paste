package com.goodworkalan.paste.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates the map of strings to strings that contains the initialization
 * parameters passed to the Paste filter.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Qualifier
public @interface InitializationParameters {
}