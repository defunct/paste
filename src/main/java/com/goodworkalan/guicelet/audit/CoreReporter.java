package com.goodworkalan.guicelet.audit;

import java.util.Map;

import com.goodworkalan.guicelet.GuiceletException;

public class CoreReporter implements Reporter
{
    private final Map<String, CoreReport> listOfReports;

    private boolean invalid;

    public CoreReporter(Map<String, CoreReport> listOfReports)
    {
        this.listOfReports = listOfReports;
    }
    
    public boolean isInvalid()
    {
        return invalid;
    }

    public Report report(String name)
    {
        if (!name.matches("[\\w][\\w\\d]+"))
        {
            throw new GuiceletException();
        }
        invalid = true;
        CoreReport report = new CoreReport();
        listOfReports.put(name, report);
        return report;
    }
    
    public Map<String, CoreReport> getReports()
    {
        return listOfReports;
    }
}
