package com.goodworkalan.sprocket.paths;

import com.goodworkalan.sprocket.RequestScoped;
import com.google.inject.Inject;
import com.google.inject.Injector;

// TODO Document.
@RequestScoped
public class PathFormatter
{
    // TODO Document.
    private final Injector injector;
 
    // TODO Document.
    @Inject
    public PathFormatter(Injector injector)
    {
        this.injector = injector;
    }
    
    // TODO Document.
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
