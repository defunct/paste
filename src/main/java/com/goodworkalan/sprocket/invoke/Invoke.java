package com.goodworkalan.sprocket.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// TODO Document.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invoke
{
    // TODO Document.
    String[] on() default {};
    
    // TODO Document.
    String param() default "";
    
    // TODO Document.
    String[] methods() default {}; 
    
    // TODO Document.
    String[] arguments() default {};
}