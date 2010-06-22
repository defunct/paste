package com.goodworkalan.paste.controller.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Indicates that an object is associated with the HTTP request, also to
 * distinguish objects provided during a nested invocation of the filter from
 * the objects provided to the request as a whole. Query parameters for internal
 * includes or forwards, for example, are bound with the {@link Filter}
 * annotation while the query parameters set by the client are bound to
 * <code>Request</code>.
 * 
 * @author Alan Gutierrez
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface Request {
}
