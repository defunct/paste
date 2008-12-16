package com.goodworkalan.guicelet.validation;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.goodworkalan.guicelet.RequestScoped;

public class MinimumLengthValidator extends AbstractValidator
{
    private final PostTree tree;
    
    private final Map<String, String> errors;
    
    public MinimumLengthValidator(PostTree tree, @RequestScoped @Errors Map<String, String> errors)
    {
        this.tree = tree;
        this.errors = errors;
    }
    
    @Override
    protected void validate(Annotation annotation, PathFixup fixup)
    {
        MinimumLength minimumLength = (MinimumLength) annotation;
        for (Property property : minimumLength.property())
        {
            String foreach = fixup.fixup(property.foreach());
            if (property.path().length == 0)
            {
                String value = tree.getString(foreach);
                if (value == null || value.length() < minimumLength.value())
                {
                    errors.put("minimumLength", foreach);
                }
            }
            else
            {
                for (String each : tree.find(foreach))
                {
                    for (String path : property.path())
                    {
                        String string = tree.getString(each + '.' + path);
                        if (string == null || string.length() < minimumLength.value())
                        {
                            errors.put("minimumLength", foreach);
                        }
                    }
                }
            }
        }
    }
}
