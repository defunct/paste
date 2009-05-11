package com.goodworkalan.sprocket.audit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.infuse.PathException;
import com.goodworkalan.infuse.PropertyGlob;
import com.goodworkalan.infuse.PropertyPath;
import com.goodworkalan.sprocket.SprocketException;
import com.goodworkalan.sprocket.faults.Fault;
import com.goodworkalan.sprocket.faults.FaultMessages;

// TODO Document.
public class Auditor
{
    // TODO Document.
    private final Object controller;

    // TODO Document.
    private final List<AuditAction> listOfAuditActions;

    // TODO Document.
    public Auditor(Object controller)
    {
        this.controller = controller;
        this.listOfAuditActions = new ArrayList<AuditAction>();
    }
    
    // TODO Document.
    public AuditPath audit(String contextPath, String path)
    {
        List<AuditBuilder> listOfAuditBuilders = new ArrayList<AuditBuilder>();
        AuditPath auditPath = new AuditPath(listOfAuditBuilders);
        listOfAuditActions.add(new AuditAction(listOfAuditBuilders, contextPath, path));
        return auditPath;
    }

    // TODO Document.
    public AuditPath audit(String path)
    {
        return audit("this", path);
    }
    
    // FIXME Move to AuditActor.
    // TODO Document.
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
            throw new SprocketException(e);
        }
    }
}
