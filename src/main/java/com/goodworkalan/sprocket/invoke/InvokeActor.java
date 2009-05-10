package com.goodworkalan.sprocket.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.sprocket.Actor;
import com.goodworkalan.sprocket.Annotations;
import com.goodworkalan.sprocket.SprocketException;
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
                    PropertyPath path;
                    try
                    {
                        path = new PropertyPath(argument);
                    }
                    catch (PathException e)
                    {
                        throw new SprocketException(e);
                    }
                    try
                    {
                        arguments.add(path.get(controller));
                    }
                    catch (PathException e)
                    {
                        throw new SprocketException(e);
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
                    throw new SprocketException(e);
                }
                
            }
        }

        return null;
    }
}