package com.goodworkalan.guicelet.audit;

public class BuiltAuditBuilder extends AuditBuilder
{
    private final Audit audit;
    
    public BuiltAuditBuilder(AuditPath auditPath, Audit audit)
    {
        super(auditPath);
        this.audit = audit;
    }
    
    public Audit newAudit()
    {
        return audit;
    }
}
