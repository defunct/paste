package com.goodworkalan.paste.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Annotations;
import com.goodworkalan.paste.PasteException;
import com.google.inject.Inject;

// TODO Document.
public class InvokeActor implements Actor
{
    // TODO Document.
    private final Annotations annotations;
    
    // TODO Document.
    @Inject
    public InvokeActor(Annotations annotations)
    {
        this.annotations = annotations;
    }

    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        for (Method method : controller.getClass().getMethods())
        {
            Invoke invoke = method.getAnnotation(Invoke.class);
            if (invoke != null && annotations.invoke(invoke.on(), invoke.param(), invoke.methods()))
            {
                List<Object> arguments = new ArrayList<Object>();
                for (String argument : invoke.arguments())
                {
                    try
                    {
                        arguments.add(new Diffusion(controller).get(argument));
                    }
                    catch (PathException e)
                    {
                        throw new PasteException(e);
                    }
                }
                try
                {
                    method.invoke(controller, arguments.toArray());
                }
                catch (InvocationTargetException e)
                {
                    return e.getCause();
                }
                catch (Exception e)
                {
                    throw new PasteException(e);
                }
                
            }
        }

        return null;
    }
}
