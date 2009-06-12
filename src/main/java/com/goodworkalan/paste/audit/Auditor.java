package com.goodworkalan.paste.audit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.goodworkalan.infuse.Diffusion;
import com.goodworkalan.infuse.Infusion;
import com.goodworkalan.infuse.Path;
import com.goodworkalan.infuse.PathException;
import com.goodworkalan.paste.PasteException;
import com.goodworkalan.paste.faults.Fault;
import com.goodworkalan.paste.faults.FaultMessages;

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
                Diffusion glob = new Diffusion(request);
                for (Path contextPath : glob.all(action.getContextPath()))
                {
                    Object context = glob.get(contextPath);
                    Object value = new Diffusion(context).get(action.getPath());
                    
                    String keyStem = contextPath.withoutIndexes();
                    if (keyStem.equals("this"))
                    {
                        keyStem = contextPath.appendAll(new Path(action.getPath(), false)).withoutIndexes();
                    }
                    else
                    {
                        keyStem += "." + contextPath.appendAll(new Path(action.getPath(), false)).withoutIndexes();
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
                                Infusion.getInstance(faults).infuse(new com.goodworkalan.infuse.Tree().add(contextPath + "." + contextPath.appendAll(new Path(action.getPath(), false)), new Fault(message, report.map)));
                            }
                        }
                    }
                }
            }
        }
        catch (PathException e)
        {
            throw new PasteException(e);
        }
    }
}
