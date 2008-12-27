package com.goodworkalan.guicelet.audit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.dspl.PathException;
import com.goodworkalan.dspl.PropertyGlob;
import com.goodworkalan.dspl.PropertyPath;
import com.goodworkalan.guicelet.GuiceletException;
import com.goodworkalan.guicelet.faults.Fault;
import com.goodworkalan.guicelet.faults.FaultMessages;

public class Auditor
{
    private final Object controller;

    private final List<AuditAction> listOfAuditActions;

    public Auditor(Object controller)
    {
        this.controller = controller;
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
    
    // FIXME Move to AuditActor.
    public void audit(Map<Object, Object> faults, Map<Object, Object> request)
    {
        try
        {
            Map<String, CoreReport> reports = new LinkedHashMap<String, CoreReport>();
            for (AuditAction action : listOfAuditActions)
            {
                PropertyGlob glob = new PropertyGlob(action.getContextPath());
                for (PropertyPath contextPath : glob.all(request))
                {
                    Object context = contextPath.get(request);
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

                    Tree tree = new Tree(context, value, request);
                    CoreReporter reporter = new CoreReporter(reports);
                    for (AuditBuilder newAudit : action.getAuditBuilders())
                    {
                        newAudit.newAudit().audit(reporter, tree);
                        if (reporter.isInvalid())
                        {
                            FaultMessages messages = new FaultMessages(controller);
                            for (String key : reporter.getReports().keySet())
                            {
                                String message = messages.getMessage(keyStem, key);
                                CoreReport report = reporter.getReports().get(key);
                                report.put("value", value);
                                new PropertyPath(contextPath + "." + path).set(faults, new Fault(message, report.map), true);
                            }
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
