package com.goodworkalan.guicelet.audit;

public interface Report
{
    public String getKeyStem();
    
    public void report(String name);
    
    public Report put(String name, Object object);
}
