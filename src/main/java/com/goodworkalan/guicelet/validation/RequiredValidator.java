package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;
import com.google.inject.Inject;

public class RequiredValidator extends AbstractFieldValidator
{
    @Inject
    public RequiredValidator(PostTree tree, @RequestScoped @Faults Map<String, Fault> errors, FaultFactory faultFactory)
    {
        super("required", tree, errors, faultFactory);
    }
    
    @Override
    protected boolean isValid(Annotation annotation, PostTree tree, String path)
    {
        return tree.getObject(path) != null;
    }
}
