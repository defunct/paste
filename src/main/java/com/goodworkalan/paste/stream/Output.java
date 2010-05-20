package com.goodworkalan.paste.stream;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to specify the mime-type of a controller method that emits an
 * output stream.
 * 
 * @author Alan Gutierrez
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Output {
    String contentType() default "application/octet-stream";
}
