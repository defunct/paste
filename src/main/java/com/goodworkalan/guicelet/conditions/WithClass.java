package com.goodworkalan.guicelet.conditions;

import com.goodworkalan.diverge.Condition;

public class WithClass implements Condition
{
    private final Condition condition;
    
    public WithClass(Condition condition)
    {
        this.condition = condition;
    }

    public boolean test(Object object)
    {
        return condition.test(object.getClass());
    }
    
    @Override
    public boolean equals(Object object)
    {
        if (object instanceof WithClass)
        {
            WithClass withClass = (WithClass) object;
            return condition.equals(withClass.condition);
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return condition.hashCode();
    }
}
