package com.goodworkalan.paste.invoke;

import java.lang.reflect.Method;

import javax.inject.Inject;

import com.goodworkalan.ilk.inject.Boxed;
import com.goodworkalan.ilk.inject.InjectException;
import com.goodworkalan.ilk.inject.Injector;
import com.goodworkalan.paste.Annotations;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.PasteException;
import com.goodworkalan.reflective.ReflectiveException;

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
                    injector.inject(controller.box, method);
                } catch (InjectException e) {
                    if (e.getCode() == ReflectiveException.INVOCATION_TARGET) {
                        throw new PasteException(PasteException.ACTOR_EXCEPTION, e);
                    }
                    throw e;
                }
            }
        }
    }
}
