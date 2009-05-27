package com.goodworkalan.paste.validate;

public @interface IntegerInRange
{
    int minimum() default Integer.MAX_VALUE;
    
    int maxiumum() default Integer.MAX_VALUE;
}
