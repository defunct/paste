package com.goodworkalan.guicelet.audit;

import java.util.Map;

import com.goodworkalan.guicelet.GuiceletException;

public class CoreReporter implements Reporter
{
    private final Map<String, CoreReport> listOfReporters;

    private final String keyStem;
    
    private final Object value;
    
    private boolean invalid;

    public CoreReporter(Map<String, CoreReport> listOfReports, String keyStem, Object value)
    {
        this.listOfReporters = listOfReports;
        this.keyStem = keyStem;
        this.value = value;
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
        listOfReporters.put(keyStem + "." + name, report);
        report.put("value", value);
        return report;
    }
}
