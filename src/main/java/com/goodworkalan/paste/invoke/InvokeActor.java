package com.goodworkalan.paste.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.inject.Inject;

import com.goodworkalan.ilk.IlkReflect;
import com.goodworkalan.ilk.inject.Boxed;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.actor.ControllerException;
import com.goodworkalan.paste.controller.Annotations;
import com.goodworkalan.paste.controller.PasteException;
import com.goodworkalan.paste.controller.qualifiers.Controller;
import com.goodworkalan.reflective.Reflective;

// TODO Document.
public class InvokeActor implements Runnable {
    // TODO Document.
    private final Annotations annotations;
    
    // TODO Document.
    private final Injector injector;
    
    // TODO Document.
    private final Boxed<Object> controller;

    // TODO Document.
    @Inject
    public InvokeActor(Injector injector, @Controller Boxed<Object> controller, Annotations annotations) {
        this.injector = injector;
        this.annotations = annotations;
        this.controller = controller;
    }

    // TODO Document.
    public void run() {
        for (Method method : controller.box.object.getClass().getMethods()) {
            Invoke invoke = method.getAnnotation(Invoke.class);
            if (invoke != null && annotations.invoke(invoke.on(), invoke.param(), invoke.methods())) {
                try {
                    injector.inject(IlkReflect.REFLECTOR, controller.box, method);
                } catch (InvocationTargetException e) {
                    throw new ControllerException(e);
                } catch (Throwable e) {
                    throw new PasteException(Reflective.encode(e), e);
                }
            }
        }
    }
}
