package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;

public class ForNames implements ViewCondition
{
    private final String[] names;
    
    public ForNames(String... names)
    {
        this.names = names;
    }

    public boolean test(Class<? extends Annotation> bundle, String name)
    {
        for (int i = 0; i < names.length; i++)
        {
            if (names[i].equals(name))
            {
                return true;
            }
        }
        return false;
    }
}
