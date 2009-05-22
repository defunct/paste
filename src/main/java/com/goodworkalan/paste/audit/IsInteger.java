package com.goodworkalan.paste.audit;

// TODO Document.
public class IsInteger extends AuditBuilder implements Audit
{
    // TODO Document.
    public IsInteger(AuditPath auditPath)
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
    private boolean valid(Object object)
    {
        if (object instanceof Integer)
        {
            return true;
        }
        else if ((object instanceof String) && object != null && "".equals(object))
        {
            try
            {
                Integer.parseInt((String) object);
                return true;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        }
        return false;
    }
    
    // TODO Document.
    public void audit(Reporter reporter, Tree tree)
    {
        if (!valid(tree.getValue()))
        {
            reporter.report("isInteger");
        }
    }
}
