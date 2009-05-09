package com.goodworkalan.sprocket.audit;

// TODO Document.
public interface Report
{
    // TODO Document.
    public String getKeyStem();
    
    // TODO Document.
    public void report(String name);
    
    // TODO Document.
    public Report put(String name, Object object);
}
