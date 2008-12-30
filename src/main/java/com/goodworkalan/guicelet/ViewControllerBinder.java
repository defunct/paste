package com.goodworkalan.guicelet;

import java.util.List;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;
import com.goodworkalan.guicelet.conditions.WithClass;

public class ViewControllerBinder extends ViewConditionBinder
{
    public ViewControllerBinder(ViewConditionBinder parent, RuleMapBuilder<ViewBinding> mapOfRules,  List<RuleSetBuilder<ViewBinding>> setOfRules)
    {
        super(parent, mapOfRules, setOfRules);
    }
    
    public ViewControllerBinder or(Class<?> controllerClass)
    {
        listOfSetOfRules.get(0).check(PatternKey.CONTROLLER, new WithClass(new Equals(controllerClass)));
        return this;
    }
}
