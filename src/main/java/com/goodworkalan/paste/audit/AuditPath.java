package com.goodworkalan.paste.audit;

import java.lang.reflect.Constructor;
import java.util.List;

import com.goodworkalan.paste.SprocketException;

// TODO Document.
public class AuditPath
{
    // TODO Document.
    private final List<AuditBuilder> listOfAuditBuilders;
    
    // TODO Document.
    public AuditPath(List<AuditBuilder> listOfAuditBuilders)
    {
        this.listOfAuditBuilders = listOfAuditBuilders;
    }

    // TODO Document.
    public <T extends AuditBuilder> T with(Class<T> auditor)
    {
        Constructor<T> constructor;
        try
        {
            constructor = auditor.getConstructor(AuditPath.class);
        }
        catch (Exception e)
        {
            throw new SprocketException(e);
        }
        T audit;
        try
        {
            audit = constructor.newInstance(this);
        }
        catch (Exception e)
        {
            throw new SprocketException(e);
        }
        listOfAuditBuilders.add(audit);
        return audit;
    }
    
    // TODO Document.
    public AuditBuilder with(Audit audit) 
    {
        AuditBuilder newAudit =  new BuiltAuditBuilder(this, audit);
        listOfAuditBuilders.add(newAudit);
        return newAudit;
    }
}
