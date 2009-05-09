package com.goodworkalan.sprocket.forward;

import com.goodworkalan.sprocket.RequestScoped;
import com.goodworkalan.sprocket.paths.FormatArgument;

// TODO Document.
@RequestScoped
class Configuration
{
    // TODO Document.
    private final String property;
    
    // TODO Document.
    private final String format;
    
    // TODO Document.
    private final FormatArgument[] formatArguments;

    // TODO Document.
    public Configuration(String property, String format, FormatArgument[] formatArguments)
    {
        this.property = property;
        this.format = format;
        this.formatArguments = formatArguments;
    }
    
    // TODO Document.
    public String getProperty()
    {
        return property;
    }
    
    // TODO Document.
    public String getFormat()
    {
        return format;
    }
    
    // TODO Document.
    public FormatArgument[] getFormatArguments()
    {
        return formatArguments;
    }
}