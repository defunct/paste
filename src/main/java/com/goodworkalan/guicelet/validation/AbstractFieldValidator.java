package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;

public abstract class AbstractFieldValidator extends AbstractValidator
{
    private final String name;
    
    private final FaultFactory faultFactory;

    private final PostTree tree;
    
    private final Map<String, Fault> faults;

    public AbstractFieldValidator(String name, PostTree tree, @RequestScoped @Faults Map<String, Fault> faults, FaultFactory faultFactory)
    {
        this.name = name;
        this.faultFactory = faultFactory;
        this.tree = tree;
        this.faults = faults;
    }
    
    protected abstract boolean isValid(Annotation annotation, PostTree tree, String path);
    
    protected void atFault(Annotation annotation, Fault fault)
    {
    }
    
    private void atFault(Annotation annotation, String context, String path)
    {
        String key = context + '.' + name;
        Fault fault = faultFactory.newFault(key, path);
        atFault(annotation, fault);
        faults.put(key, fault);
    }
    
    @Override
    protected void validate(Annotation annotation, PathFixup fixup, String context)
    {
        Required required = (Required) annotation;
        for (Property property : required.property())
        {
            String foreach = fixup.fixup(property.foreach());
            if (property.path().length == 0)
            {
                if (!isValid(annotation, tree, foreach))
                {
                    atFault(annotation, context, foreach);
                }
            }
            else
            {
                for (String each : tree.find(foreach))
                {
                    for (String path : property.path())
                    {
                        String full = each + '.' + path;
                        if (!isValid(annotation, tree, full))
                        {
                            atFault(annotation, context, full);
                        }
                    }
                }
            }
        }
    }
}
