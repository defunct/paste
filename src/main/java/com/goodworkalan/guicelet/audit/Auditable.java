package com.goodworkalan.guicelet.audit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable
{
    String[] on() default {};
    
    String param() default "";
    
    String[] methods() default {};
}
