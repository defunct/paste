package com.goodworkalan.paste.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates that an object was created based on the current invocation of the
 * filter. Used to annotate the criteria with which the filter was invoked and
 * the list of janitors that will clean up when the invocation completes.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Filter {
}
