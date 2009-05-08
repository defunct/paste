package com.goodworkalan.guicelet.audit;

import java.util.List;

// TODO Document.
public class AuditAction
{
    // TODO Document.
    private final List<AuditBuilder> listOfAuditBuilders;

    // TODO Document.
    private final String contextPath;
    
    // TODO Document.
    private final String path;
    
    // TODO Document.
    public AuditAction(List<AuditBuilder> listOfAuditBuilders, String contextPath, String path)
    {
        this.listOfAuditBuilders = listOfAuditBuilders;
        this.contextPath = contextPath;
        this.path = path;
    }
    
    // TODO Document.
    public List<AuditBuilder> getAuditBuilders()
    {
        return listOfAuditBuilders;
    }
    
    // TODO Document.
    public String getContextPath()
    {
        return contextPath;
    }
    
    // TODO Document.
    public String getPath()
    {
        return path;
    }
}