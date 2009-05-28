package com.goodworkalan.paste.infuse;

import com.goodworkalan.infuse.Infusion;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.paste.Actor;
import com.goodworkalan.paste.Controller;
import com.goodworkalan.paste.Parameters;
import com.google.inject.Inject;

/**
 * An actor that sets bean properties in a controller.
 * 
 * @author Alan Gutierrez
 */
public class InfusionActor implements Actor
{
    /** The controller parameters. */
    private final Parameters parameters;
    
    /** Construct an infusion actor with the given controller parameters.
     * 
     * @param parameters Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(@Controller Parameters parameters)
    {
        this.parameters = parameters;
    }
    
    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        for (String key : parameters.getNames())
        {
            String value = parameters.getFirst(key);
            if (value != null)
            {
                Infusion path = null;
                try
                {
                    path = new Infusion(key, value);
                }
                catch (PathException e)
                {
                    continue;
                }
                try
                {
                    path.infuse(controller);
                }
                catch (PathException e)
                {
                }
            }
        }
        
        return null;
    }
}