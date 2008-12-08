package com.goodworkalan.guicelet.redirect;

import com.goodworkalan.guicelet.paths.FormatArgument;

class Configuration
{
    private final int status;
    
    private final String format;
    
    private final FormatArgument[] formatArguments;
    
    public Configuration(int status, String format, FormatArgument[] formatArguments)
    {
        this.status = status;
        this.format = format;
        this.formatArguments = formatArguments;
    }
    
    public int getStatus()
    {
        return status;
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
