package com.goodworkalan.guicelet.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@BindingAnnotation
public @interface Tree
{

}
