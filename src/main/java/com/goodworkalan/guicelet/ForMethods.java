package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForMethods implements ControllerCondition
{
    private final String[] methods;
    
    public ForMethods(String... methods)
    {
        this.methods = methods;
    }
    
    public boolean test(HttpServletRequest request, HttpServletResponse response)
    {
        for (String method : methods)
        {
            if (method.equals(request.getMethod()))
            {
                return true;
            }
        }
        return false;
    }
}
