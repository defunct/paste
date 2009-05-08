package com.goodworkalan.guicelet.audit;

// TODO Document.
public class ValueInRange extends AuditBuilder
{
    // TODO Document.
    private long min = Long.MIN_VALUE;
    
    // TODO Document.
    private long max = Long.MAX_VALUE;
    
    // TODO Document.
    public ValueInRange(AuditPath path)
    {
        super(path);
    }
    
    // TODO Document.
    public ValueInRange min(long min)
    {
        this.min = min;
        return this;
    }
    
    // TODO Document.
    public ValueInRange max(long max)
    {
        this.max = max;
        return this;
    }

    // TODO Document.
    @Override
    public Audit newAudit()
    {
        return new Audit()
        {
            public void audit(Reporter reporter, Tree tree)
            {
                long value = Long.parseLong((String) tree.getValue());
                if (value < min || value > max)
                {
                    reporter.report("valueInRange")
                            .put("min", min)
                            .put("max", max);
                }
            }
        };
    }
}
