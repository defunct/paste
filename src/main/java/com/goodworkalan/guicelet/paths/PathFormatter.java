package com.goodworkalan.guicelet.paths;

import com.goodworkalan.guicelet.RequestScoped;
import com.goodworkalan.guicelet.Transfer;
import com.google.inject.Inject;

@RequestScoped
public class PathFormatter
{
    private final Transfer transfer;
 
    @Inject
    public PathFormatter(Transfer transfer)
    {
        this.transfer = transfer;
    }
    
    public String format(String format, FormatArgument[] formatArguments)
    {
        Object[] arguments = new String[formatArguments.length];
        for (int i = 0; i < arguments.length; i++)
        {
            arguments[i] = formatArguments[i].getArgument(transfer);
        }
        return String.format(format, arguments);
    }
}
