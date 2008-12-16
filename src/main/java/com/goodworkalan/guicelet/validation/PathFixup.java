package com.goodworkalan.guicelet.validation;

import java.util.regex.Pattern;

public class PathFixup
{
    private final Pattern pattern;
    
    private final String replace;
    
    public PathFixup(Pattern pattern, String replace)
    {
        this.pattern = pattern;
        this.replace = replace;
    }
    
    public String fixup(String path)
    {
        return pattern.matcher(path).replaceFirst(replace);
    }
}
