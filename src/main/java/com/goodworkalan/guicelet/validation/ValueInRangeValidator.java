package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;
import com.google.inject.Inject;

public class ValueInRangeValidator extends AbstractFieldValidator
{
    @Inject
    public ValueInRangeValidator(PostTree tree, @RequestScoped @Faults Map<String, Fault> errors, FaultFactory faultFactory)
    {
        super("valueOutOfRange", tree, errors, faultFactory);
    }
    
    @Override
    protected boolean isValid(Annotation annotation, PostTree tree, String path)
    {
        String string = (String) tree.getObject(path);
        if (string != null && string.length() != 0)
        {
            ValueInRange range = (ValueInRange) annotation;
            try
            {
                long number = Long.parseLong(string);
                if (number > range.max() || number < range.min())
                {
                    return false;
                }
            }
            catch (NumberFormatException e)
            {
            }
        }
        return true;
    }
    
    @Override
    protected void atFault(Annotation annotation, Fault fault)
    {
        ValueInRange range = (ValueInRange) annotation;
        fault.getMessage().put("min", range.min()).put("max", range.max());
    }
}
