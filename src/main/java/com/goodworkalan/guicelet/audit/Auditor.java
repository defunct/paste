package com.goodworkalan.guicelet.audit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyGlob;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.GuiceletException;

public class Auditor
{
    private final List<AuditAction> listOfAuditActions;

    public Auditor()
    {
        this.listOfAuditActions = new ArrayList<AuditAction>();
    }
    
    public AuditPath audit(String contextPath, String path)
    {
        List<AuditBuilder> listOfAuditBuilders = new ArrayList<AuditBuilder>();
        AuditPath auditPath = new AuditPath(listOfAuditBuilders);
        listOfAuditActions.add(new AuditAction(listOfAuditBuilders, contextPath, path));
        return auditPath;
    }

    public AuditPath audit(String path)
    {
        return audit("this", path);
    }
    
    public void audit(Map<Object, Object> map)
    {
        try
        {
            Map<String, CoreReport> reports = new LinkedHashMap<String, CoreReport>();
            for (AuditAction action : listOfAuditActions)
            {
                PropertyGlob glob = new PropertyGlob(action.getContextPath());
                for (PropertyPath contextPath : glob.all(map))
                {
                    Object context = contextPath.get(map);
                    PropertyPath path = new PropertyPath(action.getPath());

                    Object value = path.get(context);
                    
                    String keyStem = contextPath.withoutIndexes();
                    if (keyStem.equals("this"))
                    {
                        keyStem = path.withoutIndexes();
                    }
                    else
                    {
                        keyStem += "." + path.withoutIndexes();
                    }

                    Tree tree = new Tree(context, value, map);
                    CoreReporter reporter = new CoreReporter(reports, keyStem, value);
                    for (AuditBuilder newAudit : action.getAuditBuilders())
                    {
                        newAudit.newAudit().audit(reporter, tree);
                        if (reporter.isInvalid())
                        {
                            System.out.println("Invalid.");
                        }
                    }
                }
            }
        }
        catch (PathException e)
        {
            throw new GuiceletException(e);
        }
    }
}
