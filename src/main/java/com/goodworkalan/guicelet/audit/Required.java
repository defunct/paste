package com.goodworkalan.guicelet.audit;


// TODO Document.
public class Required extends AuditBuilder implements Audit
{
    // TODO Document.
    public Required(AuditPath auditPath)
    {
        super(auditPath);
    }
    
    // TODO Document.
    @Override
    public Audit newAudit()
    {
        return this;
    }
    
    // TODO Document.
    private boolean invalid(Object object)
    {
        return object == null || "".equals(object);
    }
    
    // TODO Document.
    public void audit(Reporter reporter, Tree tree)
    {
        if (invalid(tree.getValue()))
        {
            reporter.report("required");
        }
    }
}