package com.goodworkalan.paste.infuse;

import java.util.Set;

import com.goodworkalan.infuse.InfusionBuilder;
import com.goodworkalan.infuse.ObjectFactory;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.Tree;
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
    
    private final Set<ObjectFactory> factories;
    
    /** Construct an infusion actor with the given controller parameters.
     * 
     * @param parameters Parameters with the controller parameters.
     */
    @Inject
    public InfusionActor(@Controller Parameters parameters, Set<ObjectFactory> factories)
    {
        this.parameters = parameters;
        this.factories = factories;
    }
    
    // TODO Document.
    public Throwable actUpon(Object controller)
    {
        Tree tree = new Tree();
        InfusionBuilder builder = new InfusionBuilder();
        
        builder.addFactories(factories);
        
        for (String key : parameters.getNames())
        {
            String value = parameters.getFirst(key);
            if (value != null)
            {
                try
                {
                    tree.set(key, value);
                }
                catch (PathException e)
                {
                    continue;
                }
            }
            try
            {
                builder.addFactories(factories).getInstance(controller).infuse(tree);
            }
            catch (PathException e)
            {
            }
        }
        
        return null;
    }
}