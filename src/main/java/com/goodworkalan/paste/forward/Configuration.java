package com.goodworkalan.paste.forward;

import com.goodworkalan.paste.paths.ControllerClassAsPath;

/**
 * A configuration structure for the forward renderer.
 * 
 * @author Alan Gutierrez
 */
class Configuration {
    /** The format to use to create the forward path. */
    public String format = "/%s.ftl";

    /** The request property name to use to store the controller. */
    public String property = "controller";

    /** The format arguments to use to create the forward path. */
    public Class<?>[] formatArguments = new Class<?>[] { ControllerClassAsPath.class };
}