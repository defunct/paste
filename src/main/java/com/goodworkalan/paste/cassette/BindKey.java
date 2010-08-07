package com.goodworkalan.paste.cassette;

/**
 * A set of keys used to bind request values to test parameters in order to
 * match controllers and renderers against request values using Deviate.
 * 
 * @author Alan Gutierrez
 */
 public enum BindKey {
    /** The controller package. */
    PACKAGE,
    /** The controller. */
    CONTROLLER,
    /** The controller class. */
    CONTROLLER_CLASS,
    /** Huh? */
    ANNOTATION,
    /** The request method. */
    METHOD,
    /** Type content type. */
    CONTENT_TYPE, 
    /** The request path. */
    PATH,
    /** The file suffix. */
    SUFFIX,
    /** The response status. */
    STATUS,
    /** The exception thrown. */
    EXCEPTION
}
