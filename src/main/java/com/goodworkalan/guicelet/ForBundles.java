package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;

public class ForBundles implements ViewCondition
{
    private final Class<? extends Annotation> bundle;
    
    public ForBundles(Class<? extends Annotation> bundle)
    {
        this.bundle = bundle;
    }
    
    public boolean test(Class<? extends Annotation> bundle, String name)
    {
        return bundle.equals(this.bundle);
    }
}
