package com.goodworkalan.guicelet.redirect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO Document.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SuggestedRedirection
{
    public String value();
}
