package com.goodworkalan.guicelet.audit;

public interface Reporter
{
    boolean isInvalid();

    public Report report(String name);
}
