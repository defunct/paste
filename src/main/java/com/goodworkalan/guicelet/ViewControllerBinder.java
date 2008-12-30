package com.goodworkalan.guicelet;

import java.util.List;

import com.goodworkalan.diverge.Equals;
import com.goodworkalan.diverge.RuleMapBuilder;
import com.goodworkalan.diverge.RuleSetBuilder;

public class ViewControllerBinder extends ViewBinder
{
    public ViewControllerBinder(ViewBinder parent, RuleMapBuilder<ViewBinding> mapOfRules,  List<RuleSetBuilder<ViewBinding>> setOfRules)
    {
        super(parent, mapOfRules, setOfRules);
    }
    
    public ViewControllerBinder or(Class<?> controllerClass)
    {
        listOfSetOfRules.get(0).check(BindKey.CONTROLLER_CLASS, new Equals(controllerClass));
        return this;
    }
}
