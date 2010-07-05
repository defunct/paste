package com.goodworkalan.paste.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.Boxed;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.controller.Annotations;
import com.goodworkalan.paste.controller.qualifiers.Controller;

/**
 * Invokes a methods on a controller that have been annotated for invocation.
 * 
 * @author Alan Gutierrez
 */
public class InvokeActor implements Runnable {
    /**
     * The helper class that implements the logic used by actors to select a
     * method in a controller for invocation.
     */
    private final Annotations annotations;
    
    /** The injector. */
    private final Injector injector;
    
    /** The boxed controller instance. */
    private final Boxed<Object> controller;

    /**
     * Create an invocation actor.
     * 
     * @param injector
     *            The injector.
     * @param controller
     *            The boxed controller instance.
     * @param annotations
     *            The helper class that implements the logic used by actors to
     *            select a method in a controller for invocation.
     */
    @Inject
    public InvokeActor(Injector injector, @Controller Boxed<Object> controller, Annotations annotations) {
        this.injector = injector;
        this.annotations = annotations;
        this.controller = controller;
    }

    /** Invoke method on a controller. */
    public void run() {
        for (Method method : controller.box.object.getClass().getMethods()) {
            Invoke invoke = method.getAnnotation(Invoke.class);
            if (invoke != null && annotations.invoke(invoke.on(), invoke.param(), invoke.methods())) {
                try {
                    injector.inject(IlkReflect.REFLECTOR, controller.box, method);
                } catch (InvocationTargetException e) {
                    throw new ControllerException(e);
                } catch (Exception e) {
                    throw new RuntimeException(String.format("\n\tUnable to invoke controller method.\n\t\tController [%s], Method [%s]", controller.box.key, method.getName()), e);
                }
            }
        }
    }
}
