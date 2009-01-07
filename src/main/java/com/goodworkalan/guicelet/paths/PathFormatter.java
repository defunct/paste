package com.goodworkalan.guicelet.paths;

import com.goodworkalan.guicelet.RequestScoped;
import com.google.inject.Inject;
import com.google.inject.Injector;

@RequestScoped
public class PathFormatter
{
    private final Injector injector;
 
    @Inject
    public PathFormatter(Injector injector)
    {
        this.injector = injector;
    }
    
    public String format(String format, FormatArgument[] formatArguments)
    {
        Object[] arguments = new String[formatArguments.length];
        for (int i = 0; i < arguments.length; i++)
        {
            arguments[i] = formatArguments[i].getArgument(injector);
        }
        return String.format(format, arguments);
    }
}
