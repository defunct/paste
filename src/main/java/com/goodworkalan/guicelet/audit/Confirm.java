package com.goodworkalan.guicelet.audit;

import com.goodworkalan.guicelet.GuiceletException;

public class Confirm extends AuditBuilder
{
    private String compare;
    
    public Confirm(AuditPath auditPath)
    {
        super(auditPath);
    }
    
    public Confirm compare(String compare)
    {
        if (compare == null)
        {
            throw new NullPointerException(); 
        }
        this.compare = compare;
        return this;
    }

    @Override
    public Audit newAudit()
    {
        return new Audit()
        {
            public void audit(Reporter reporter, Tree tree)
            {
                if (compare == null)
                {
                    throw new GuiceletException();
                }
                String string = (String) tree.getContextValue(compare);
                if (!string.equals(tree.getValue()))
                {
                    reporter.report("confirm").put("confirmation", string);
                }
            }
        };
    }
}