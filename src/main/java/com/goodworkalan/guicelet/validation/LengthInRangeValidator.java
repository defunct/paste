package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;

public class LengthInRangeValidator extends AbstractFieldValidator
{
    public LengthInRangeValidator(PostTree tree, @RequestScoped @Faults Map<String, Fault> errors, FaultFactory faultFactory)
    {
        super("lengthOutOfRange", tree, errors, faultFactory);
    }

    @Override
    protected boolean isValid(Annotation annotation, PostTree tree, String path)
    {
        String string = (String) tree.getObject(path);
        if (string != null)
        {
            LengthInRange range = (LengthInRange) annotation;
            if (string.length() > range.max() || string.length() < range.min())
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void atFault(Annotation annotation, Fault fault)
    {
        LengthInRange range = (LengthInRange) annotation;
        fault.getMessage().put("min", range.min()).put("max", range.max());
    }}
