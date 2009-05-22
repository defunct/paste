package com.goodworkalan.paste;

import java.util.List;

import com.goodworkalan.dovetail.GlobCompiler;

// TODO Document.
public class ControllerBinder
{
    // TODO Document.
    private final List<ControllerPathMapping> listOfControllerPathMappings;

    // TODO Document.
    private final GlobCompiler compiler;
    
    // TODO Document.
    public ControllerBinder(GlobCompiler compiler, List<ControllerPathMapping> listOfControllerPathMappings)
    {
        this.listOfControllerPathMappings = listOfControllerPathMappings;
        this.compiler = compiler;
    }
    
    // TODO Document.
    public ControllerPathBinder bind(String pattern)
    {
        ControllerPathMapping mapping = new ControllerPathMapping();
        listOfControllerPathMappings.add(mapping);
        return new ControllerPathBinder(this, mapping.listOfGlobs, mapping.rules, compiler).or(pattern);
    }
}