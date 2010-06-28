package com.goodworkalan.paste.infuse;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates that an object can be created by the infusion actor.
 * <p>
 * In order to use this annotation you <strong>must</strong> configure it
 * yourself. Not only must you apply the annotation to your classes but you must
 * also specify the annotation in your String Beans configuration.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Infusable {
}
