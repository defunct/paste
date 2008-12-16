package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;

public class RequiredValidator extends AbstractValidator
{
    private final PostTree tree;
    
    private final Map<String, String> errors;
    
    public RequiredValidator(PostTree tree, @RequestScoped @Errors Map<String, String> errors)
    {
        this.tree = tree;
        this.errors = errors;
    }
    
    @Override
    protected void validate(Annotation annotation, PathFixup fixup)
    {
        Required required = (Required) annotation;
        for (Property property : required.property())
        {
            String foreach = fixup.fixup(property.foreach());
            if (property.path().length == 0)
            {
                if (tree.getObject(foreach) == null)
                {
                    errors.put("required", foreach);
                }
            }
            else
            {
                for (String each : tree.find(foreach))
                {
                    for (String path : property.path())
                    {
                        if (tree.getObject(each + '.' + path) == null)
                        {
                            errors.put("required", foreach);
                        }
                    }
                }
            }
        }
    }
}
