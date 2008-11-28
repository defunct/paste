package com.goodworkalan.guicelet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControllerBinding
{
    private final int priority;
    
    private final List<ControllerCondition> conditions;
    
    private final Class<?> controller;
    
    public ControllerBinding(List<ControllerCondition> conditions, int priority, Class<?> controller)
    {
        this.conditions = conditions;
        this.priority = priority;
        this.controller = controller;
    }
    
    public int getPriority()
    {
        return priority;
    }
    
    public Class<?> getController()
    {
        return controller;
    }
    
    public boolean test(HttpServletRequest request, HttpServletResponse response)
    {
        for (ControllerCondition condition : conditions)
        {
            if (!condition.test(request, response))
            {
                return false;
            }
        }
        return true;
    }
}
