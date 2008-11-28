package com.goodworkalan.guicelet;

import java.lang.annotation.Annotation;

public interface ViewCondition
{
    public boolean test(Class<? extends Annotation> bundle, String name);
}
