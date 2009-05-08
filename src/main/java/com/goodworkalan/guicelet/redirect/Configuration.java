package com.goodworkalan.guicelet.redirect;

import com.goodworkalan.guicelet.paths.FormatArgument;

// TODO Document.
class Configuration
{
    // TODO Document.
    private final int status;
    
    // TODO Document.
    private final String format;
    
    // TODO Document.
    private final FormatArgument[] formatArguments;
    
    // TODO Document.
    public Configuration(int status, String format, FormatArgument[] formatArguments)
    {
        this.status = status;
        this.format = format;
        this.formatArguments = formatArguments;
    }
    
    // TODO Document.
    public int getStatus()
    {
        return status;
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
