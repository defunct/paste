package com.goodworkalan.guicelet.audit;

import java.util.List;

public class AuditAction
{
    private final List<AuditBuilder> listOfAuditBuilders;

    private final String contextPath;
    
    private final String path;
    
    public AuditAction(List<AuditBuilder> listOfAuditBuilders, String contextPath, String path)
    {
        this.listOfAuditBuilders = listOfAuditBuilders;
        this.contextPath = contextPath;
        this.path = path;
    }
    
    public List<AuditBuilder> getAuditBuilders()
    {
        return listOfAuditBuilders;
    }
    
    public String getContextPath()
    {
        return contextPath;
    }
    
    public String getPath()
    {
        return path;
    }
}