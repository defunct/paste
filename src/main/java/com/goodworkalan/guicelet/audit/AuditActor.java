package com.goodworkalan.guicelet.audit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Actors;
import com.goodworkalan.guicelet.GuiceletException;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.RequestScoped;
import com.goodworkalan.guicelet.faults.Faults;
import com.goodworkalan.guicelet.faults.Invalid;
import com.goodworkalan.guicelet.faults.RaiseInvalid;

public class AuditActor implements Actor
{
    private final HttpServletRequest request;
    private final Map<Object, Object> faults;
    
    private final Map<Class<? extends Annotation>, Parameters> parameters;
    
    public AuditActor(HttpServletRequest request,
                      @RequestScoped @Faults Map<Object, Object> faults,
                      @RequestScoped Map<Class<? extends Annotation>, Parameters> parameters)
    {
        this.request = request;
        this.faults = faults;
        this.parameters = parameters;
    }
    
    public void actUpon(Object controller)
    {
        Parameters[] merge = new Parameters[parameters.size()];
        int index = 0;
        for (Class<? extends Annotation> key : parameters.keySet())
        {
            merge[index++] = parameters.get(key);
        }            
        
        Parameters merged = Parameters.merge(merge);
        
        Map<Object, Object> map = new HashMap<Object, Object>();
        
        for (String key : merged.keySet())
        {
            String value = merged.getFirst(key);
            
            PropertyPath path;
            try
            {
                path = new PropertyPath(key);
            }
            catch (PathException e)
            {
                continue;
            }
            try
            {
                path.set(map, value, true);
            }
            catch (PathException e)
            {
                continue;
            }
        }
        
        Auditor auditor = new Auditor(controller);
        for (Method method : controller.getClass().getMethods())
        {
            Auditable auditable = method.getAnnotation(Auditable.class);
            if (auditable != null)
            {
                boolean audit = true;
                if (auditable.on() != null)
                {
                    if (auditable.param() != null)
                    {
                        audit = auditable.on().equals(merged.getFirst(auditable.param()));
                    }
                    else
                    {
                        audit = merged.containsKey(auditable.on());
                    }
                }
                if (audit)
                {
                    if (auditable.methods().length != 0)
                    {
                        audit = false;
                        for (String m : auditable.methods())
                        {
                            if (m.equals(request.getMethod()))
                            {
                                audit = true;
                                break;
                            }
                        }
                    }
                }
                if (audit)
                {
                    try
                    {
                        method.invoke(controller, auditor);
                    }
                    catch (Exception e)
                    {
                        throw new GuiceletException(e);
                    }
                    auditor.audit(faults, map);
                }
            }
        }
        
        boolean raise = true;
        Actors actors = controller.getClass().getAnnotation(Actors.class);
        for (Class<? extends Actor> actor : actors.value())
        {
            if (actor.equals(RaiseInvalid.class))
            {
                raise = false;
                break;
            }
        }

        if (raise && faults.size() != 0)
        {
            throw new Invalid();
        }
    }
}
