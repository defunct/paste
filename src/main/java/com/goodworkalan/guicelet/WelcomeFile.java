package com.goodworkalan.guicelet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

/**
 * Indicates a string to use as a welcome file for path formatting arguments
 * based on request URI when there is no file part of the path.
 * 
 * @author Alan Gutierrez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation
public @interface WelcomeFile
{
}