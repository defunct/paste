package com.goodworkalan.sprocket.audit;

// TODO Document.
public class BuiltAuditBuilder extends AuditBuilder
{
    // TODO Document.
    private final Audit audit;
    
    // TODO Document.
    public BuiltAuditBuilder(AuditPath auditPath, Audit audit)
    {
        super(auditPath);
        this.audit = audit;
    }
    
    // TODO Document.
    public Audit newAudit()
    {
        return audit;
    }
}
