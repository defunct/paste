package com.goodworkalan.guicelet;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;
import com.goodworkalan.guicelet.conditions.WithClass;

public class ViewControllerBinder extends ViewConditionBinder
{
    public ViewControllerBinder(ViewBinder viewBinder, RuleMapBuilder<ViewBinding> viewBindings,  RuleSetBuilder<ViewBinding> setOfRules)
    {
        super(viewBinder, viewBindings, setOfRules);
    }
    
    public ViewControllerBinder or(Class<?> controllerClass)
    {
        setOfRules.check(PatternKey.CONTROLLER, new WithClass(new Equals(controllerClass)));
        return this;
    }
}
