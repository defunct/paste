package com.goodworkalan.paste.audit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.PropertyPath;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.Annotations;
import com.goodworkalan.paste.Parameters;
import com.goodworkalan.paste.ParametersServer;
import com.goodworkalan.paste.SprocketException;
import com.goodworkalan.paste.faults.Faults;
import com.goodworkalan.paste.faults.Invalid;
import com.goodworkalan.paste.faults.RaiseInvalid;
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
    public Throwable actUpon(Object controller)
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
                    throw new SprocketException(e);
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
        
        return null;
    }
}
