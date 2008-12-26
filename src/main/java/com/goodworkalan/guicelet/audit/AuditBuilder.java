package com.goodworkalan.guicelet.audit;


public abstract class AuditBuilder
{
    protected final AuditPath auditPath;

    public AuditBuilder(AuditPath auditPath)
    {
        this.auditPath = auditPath;
    }
    
    public AuditPath then()
    {
        return auditPath;
    }
    
    public abstract Audit newAudit();
}
