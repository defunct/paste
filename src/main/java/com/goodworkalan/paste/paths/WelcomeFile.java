package com.goodworkalan.paste.paths;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates the welcome file to use with the {@link PathFile} argument when the
 * path ends with a slash.
 * <p>
 * I could never find the welcome file in the Servlet API, but it just now
 * occurred to me that I can find it by opening web.xml and reading through the
 * XML, if I am that determined.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface WelcomeFile {
}
