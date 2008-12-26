package com.goodworkalan.guicelet.audit;


public class Required extends AuditBuilder implements Audit
{
    public Required(AuditPath auditPath)
    {
        super(auditPath);
    }
    
    @Override
    public Audit newAudit()
    {
        return this;
    }
    
    private boolean invalid(Object object)
    {
        return object == null || "".equals(object);
    }
    
    public void audit(Reporter reporter, Tree tree)
    {
        if (invalid(tree.getValue()))
        {
            reporter.report("required");
        }
    }
}