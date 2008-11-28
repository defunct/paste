package com.goodworkalan.guicelet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ControllerCondition
{
    public boolean test(HttpServletRequest request, HttpServletResponse response);
}
