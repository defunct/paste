package com.goodworkalan.paste.validate;

public @interface LongInRange
{
    public long minimum() default Long.MIN_VALUE;
    
    public long maxiumum() default Long.MAX_VALUE;
}
