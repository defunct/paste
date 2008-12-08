package com.goodworkalan.guicelet.forward;

import com.goodworkalan.guicelet.paths.FormatArgument;

class Configuration
{
    private final String property;
    
    private final String format;
    
    private final FormatArgument[] formatArguments;

    public Configuration(String property, String format, FormatArgument[] formatArguments)
    {
        this.property = property;
        this.format = format;
        this.formatArguments = formatArguments;
    }
    
    public String getProperty()
    {
        return property;
    }
    
    public String getFormat()
    {
        return format;
    }
    
    public FormatArgument[] getFormatArguments()
    {
        return formatArguments;
    }
}