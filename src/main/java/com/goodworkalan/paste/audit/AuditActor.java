package com.goodworkalan.paste.audit;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.goodworkalan.infuse.Infusion;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Actors;
import com.goodworkalan.paste.Annotations;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.faults.Faults;
import com.goodworkalan.paste.faults.Invalid;
import com.goodworkalan.paste.faults.RaiseInvalid;
import com.goodworkalan.paste.util.NamedValueList;
import com.goodworkalan.paste.util.Parameters;
import com.google.inject.Inject;

// TODO Document.
public class AuditActor implements Actor
{
    // TODO Document.
    private final Annotations annotations;

    // TODO Document.
    private final Map<Object, Object> faults;
    
    // TODO Document.
    private final NamedValueList parameters;
    
    // TODO Document.
    @Inject
    public AuditActor(Annotations annotations,
                      @Faults Map<Object, Object> faults,
                      @Controller Parameters parameters)
    {
        this.annotations = annotations;
        this.faults = faults;
        this.parameters = parameters;
    }
    
    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        Map<Object, Object> map = new HashMap<Object, Object>();
        
        for (String key : parameters.getNames())
        {
            String value = parameters.getFirst(key);
            
            Infusion infusion = Infusion.getInstance(map);
            try
            {
                infusion.infuse(new com.goodworkalan.infuse.Tree().add(key, value));
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
                    throw new PasteException(e);
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
