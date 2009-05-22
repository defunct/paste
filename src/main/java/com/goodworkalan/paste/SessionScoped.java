package com.goodworkalan.paste;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.ScopeAnnotation;

// TODO Document.
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@ScopeAnnotation
public @interface SessionScoped
{
}
