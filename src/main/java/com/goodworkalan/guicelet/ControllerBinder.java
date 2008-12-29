package com.goodworkalan.guicelet;

import java.util.List;

import com.goodworkalan.dovetail.GlobCompiler;

public class ControllerBinder
{
    private final List<ControllerPathMapping> listOfControllerPathMappings;

    private final GlobCompiler compiler;
    
    public ControllerBinder(GlobCompiler compiler, List<ControllerPathMapping> listOfControllerPathMappings)
    {
        this.listOfControllerPathMappings = listOfControllerPathMappings;
        this.compiler = compiler;
    }
    
    public ControllerPathBinder bind(String pattern)
    {
        ControllerPathMapping mapping = new ControllerPathMapping();
        listOfControllerPathMappings.add(mapping);
        return new ControllerPathBinder(this, mapping.listOfGlobs, mapping.rules, compiler).or(pattern);
    }
}