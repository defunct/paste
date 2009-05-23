package com.goodworkalan.paste.audit;

import java.util.Map;

import com.goodworkalan.paste.PasteException;

// TODO Document.
public class CoreReporter implements Reporter
{
    // TODO Document.
    private final Map<String, CoreReport> listOfReports;

    // TODO Document.
    private boolean invalid;

    // TODO Document.
    public CoreReporter(Map<String, CoreReport> listOfReports)
    {
        this.listOfReports = listOfReports;
    }
    
    // TODO Document.
    public boolean isInvalid()
    {
        return invalid;
    }

    // TODO Document.
    public Report report(String name)
    {
        if (!name.matches("[\\w][\\w\\d]+"))
        {
            throw new PasteException();
        }
        invalid = true;
        CoreReport report = new CoreReport();
        listOfReports.put(name, report);
        return report;
    }
    
    // TODO Document.
    public Map<String, CoreReport> getReports()
    {
        return listOfReports;
    }
}
