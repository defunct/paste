package com.goodworkalan.sprocket.text;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface TextOutput
{
    String name() default "";

    String contentType() default "text/plain";
}
