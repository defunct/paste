package com.goodworkalan.guicelet.audit;

public class ValueInRange extends AuditBuilder
{
    private long min = Long.MIN_VALUE;
    
    private long max = Long.MAX_VALUE;
    
    public ValueInRange(AuditPath path)
    {
        super(path);
    }
    
    public ValueInRange min(long min)
    {
        this.min = min;
        return this;
    }
    
    public ValueInRange max(long max)
    {
        this.max = max;
        return this;
    }

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
