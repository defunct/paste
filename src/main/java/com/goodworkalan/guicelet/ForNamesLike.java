package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

public class ForNamesLike implements ViewCondition
{
    private final Pattern[] patterns;
    
    public ForNamesLike(String... expressions)
    {
        Pattern[] patterns = new Pattern[expressions.length];
        for (int i = 0; i < expressions.length; i++)
        {
            patterns[i] = Pattern.compile(expressions[i]);
        }
        this.patterns = patterns;
    }
    
    public boolean test(Class<? extends Annotation> bundle, String name)
    {
        for (Pattern pattern : patterns)
        {
            if (pattern.matcher(name).find())
            {
                return true;
            }
        }
        return false;
    }
}
