package com.goodworkalan.guicelet.validation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Validation(validator=RequiredValidator.class)
public @interface Required
{
    Property[] property() default { @Property() };
}
