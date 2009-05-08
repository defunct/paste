package com.goodworkalan.guicelet.audit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.Actor;
import com.goodworkalan.guicelet.Actors;
import com.goodworkalan.guicelet.Annotations;
import com.goodworkalan.guicelet.GuiceletException;
import com.goodworkalan.guicelet.Parameters;
import com.goodworkalan.guicelet.ParametersServer;
import com.goodworkalan.guicelet.faults.Faults;
import com.goodworkalan.guicelet.faults.Invalid;
import com.goodworkalan.guicelet.faults.RaiseInvalid;
import com.google.inject.Inject;

// TODO Document.
public class AuditActor implements Actor
{
    // TODO Document.
    private final Annotations annotations;

    // TODO Document.
    private final Map<Object, Object> faults;
    
    // TODO Document.
    private final ParametersServer parameters;
    
    // TODO Document.
    @Inject
    public AuditActor(Annotations annotations,
                      @Faults Map<Object, Object> faults,
                      ParametersServer parameters)
    {
        this.annotations = annotations;
        this.faults = faults;
        this.parameters = parameters;
    }
    
    // TODO Document.
    public void actUpon(Object controller)
    {
        Parameters merged = parameters.merge();
        
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
            if (auditable != null && annotations.invoke(auditable.on(), auditable.param(), auditable.methods()))
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
