package com.goodworkalan.guicelet.conditions;

import com.goodworkalan.deviate.Condition;

// TODO Document.
public class WithClass implements Condition
{
    // TODO Document.
    private final Condition condition;
    
    // TODO Document.
    public WithClass(Condition condition)
    {
        this.condition = condition;
    }

    // TODO Document.
    public boolean test(Object object)
    {
        return condition.test(object.getClass());
    }
    
    // TODO Document.
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
    
    // TODO Document.
    @Override
    public int hashCode()
    {
        return condition.hashCode();
    }
    
    // TODO Document.
    @Override
    public String toString()
    {
        return "WithClass(" + condition + ")";
    }
}
