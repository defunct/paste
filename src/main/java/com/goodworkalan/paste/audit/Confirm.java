package com.goodworkalan.paste.audit;

import com.goodworkalan.paste.PasteException;

// TODO Document.
public class Confirm extends AuditBuilder
{
    // TODO Document.
    private String compare;
    
    // TODO Document.
    public Confirm(AuditPath auditPath)
    {
        super(auditPath);
    }
    
    // TODO Document.
    public Confirm compare(String compare)
    {
        if (compare == null)
        {
            throw new NullPointerException(); 
        }
        this.compare = compare;
        return this;
    }

    // TODO Document.
    @Override
    public Audit newAudit()
    {
        // TODO Document.
        return new Audit()
        {
            public void audit(Reporter reporter, Tree tree)
            {
                if (compare == null)
                {
                    throw new PasteException();
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