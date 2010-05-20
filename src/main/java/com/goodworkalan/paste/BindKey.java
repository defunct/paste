package com.goodworkalan.paste;

/**
 * A set of keys used to bind request values to test parameters in order to
 * match controllers and renderers against request values using Deviate.
 * 
 * @author Alan Gutierrez
 */
public enum BindKey {
    /** The controller package. */
    PACKAGE, CONTROLL_ER,
    /** The controller class. */
    CONTROLLER_CLASS,
    /** Huh? */
    ANNOTATION,
    /** The request method. */
    METHOD,
    /** The request path. */
    PATH,
    /** The response status. */
    STATUS,
    /** The type of exception thrown. */
    EXCEPTION_CLASS
}
