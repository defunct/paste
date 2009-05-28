package com.goodworkalan.paste.validate;

@ValidatedBy(IntegerInRangeValidator.class)
public @interface IntegerInRange
{
    int minimum() default Integer.MAX_VALUE;
    
    int maxiumum() default Integer.MAX_VALUE;
}
