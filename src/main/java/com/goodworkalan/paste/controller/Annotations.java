package com.goodworkalan.paste.controller;

import javax.inject.Inject;

import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.paste.controller.qualifiers.Verb;
import com.goodworkalan.paste.controller.scopes.ControllerScoped;

/**
 * A helper class that implements the logic used by actors to select a method in
 * a controller for invocation. The {@link #invoke invoke} method determines if
 * a method should be invoked given a set of parameters obtained form an actor
 * specific annotation on the controller.
 * <p>
 * The class is created from dependency injection and used as a parameter to the
 * constructor of actor implementations.
 * 
 * @author Alan Gutierrez
 */
@ControllerScoped
public class Annotations {
    /** The request method. */
    private final String method;
    
    /** The controller parameters. */
    private final Parameters parameters;

    /**
     * Create an annotations evaluator with the given controller parameters and
     * the given request.
     * 
     * @param parameters
     *            The controller parameters.
     * @param method
     *            The request method.
     */
    @Inject
    public Annotations(@Controller Parameters parameters, @Verb String method) {
        this.parameters = parameters;
        this.method = method;
    }

    /**
     * Determine if a method should be invoked by checking annotation provided
     * parameters.
     * <p>
     * The given parameter name is check checked and if the value matches any of
     * the values in the list given by the on parameter the method is a
     * candidate for invocation. If the list of values given by the on parameter
     * is empty, this test is skipped and invoke returns true if the request
     * method test returns true.
     * <p>
     * After the parameter check, the request method is checked. If the
     * parameter check passed or was skipped, the request method check is
     * performed. If the list of methods is empty, or if the request method
     * matches one of the given methods, invoke returns true.
     * 
     * @param on
     *            The parameter values that indicate that the method should be
     *            invoked.
     * @param param
     *            The parameter to check.
     * @param methods
     *            The request method types that indicate that the method should
     *            be invoked.
     * @return True if the method should be invoked.
     */
    public boolean invoke(String[] on, String param, String[] methods) {
        boolean invoke = on.length == 0;
        if (!invoke) {
            if (!"".equals(param)) {
                String value = parameters.get(param);
                if (value != null) {
                    for (int i = 0; !invoke && i < on.length; i++) {
                        invoke = on[i].equals(value);
                    }
                }
            } else {
                for (int i = 0; !invoke && i < on.length; i++) {
                    invoke = parameters.contains(on[i]);
                }
            }
        }
        if (invoke) {
            if (methods.length != 0) {
                invoke = false;
                for (int i = 0; !invoke && i < methods.length; i++) {
                    invoke = methods[i].equals(method);
                }
            }
        }
        return invoke;
    }
}
