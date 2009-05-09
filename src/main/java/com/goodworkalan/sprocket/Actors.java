package com.goodworkalan.sprocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO Document.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Actors
{
    // TODO Document.
    Class<? extends Actor>[] value();
}
