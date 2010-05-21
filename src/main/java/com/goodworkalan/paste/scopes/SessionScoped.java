package com.goodworkalan.paste.scopes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Scope;

// TODO Document.
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Scope
public @interface SessionScoped {
}
