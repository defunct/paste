package com.goodworkalan.guicelet;

public interface Binder
{
    public ControllerBinder controllers(Class<?> conditional);

    public ViewBinder view();
}
