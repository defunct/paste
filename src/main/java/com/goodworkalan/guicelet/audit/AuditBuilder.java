package com.goodworkalan.guicelet.audit;

// TODO Document.
public abstract class AuditBuilder
{
    // TODO Document.
    protected final AuditPath auditPath;

    // TODO Document.
    public AuditBuilder(AuditPath auditPath)
    {
        this.auditPath = auditPath;
    }
    
    // TODO Document.
    public AuditPath then()
    {
        return auditPath;
    }
    
    // TODO Document.
    public abstract Audit newAudit();
}
