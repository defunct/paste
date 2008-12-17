package com.goodworkalan.guicelet.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LengthInRange
{
    long min() default Long.MIN_VALUE;
    
    long max() default Long.MAX_VALUE;
    
    Property[] property() default { @Property() };
}
