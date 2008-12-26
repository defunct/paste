package com.goodworkalan.guicelet.audit;


public class IsInteger extends AuditBuilder implements Audit
{
    public IsInteger(AuditPath auditPath)
    {
        super(auditPath);
    }

    @Override
    public Audit newAudit()
    {
        return this;
    }
    
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
    
    public void audit(Reporter reporter, Tree tree)
    {
        if (!valid(tree.getValue()))
        {
            reporter.report("isInteger");
        }
    }
}
